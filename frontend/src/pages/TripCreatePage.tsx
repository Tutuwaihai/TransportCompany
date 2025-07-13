import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";
import { fetchTransports } from "../api/dictionariesApi";
import "./TripCreatePage.css";

interface CityDto { id: number; title: string; }
interface WarehouseDto { id: number; title: string; }
interface TripTypeDto { id: number; title: string; }

interface TransportDto {
  id: number;
  model: string;
  licensePlate: string;
  tonnage: number;
  cityTitle: string;
  modelType?: number;
  ownerType?: number;
}

interface SendingMovingDto {
  id: number;
  idSending: number;
  typePointer: number;
  idPointer: number | null;
  numCount?: number | null;
  numPlaces?: number | null;
  sending?: SendingShortDto | null;
}

interface SendingShortDto {
  id: number;
  issueDate: string | null;
  lastNotification: string | null;
  sendDate: string | null;
  docNum: string | null;
  cargoName: string | null;
  packaging: string | null;
  description: string | null;
  clientFrom: ClientShortDto | null;
  clientTo: ClientShortDto | null;
  cityFrom: CityDto | null;
  cityTo: CityDto | null;
  price: PriceShortDto | null;
  wareFrom: WarehouseDto | null;
  wareTo: WarehouseDto | null;
}

interface ClientShortDto {
  id: number;
  title: string | null;
}

interface PriceShortDto {
  id: number;
  priceVolume: number | null;
}

const InvoiceTable: React.FC<{
  data: any[];
  title: string;
  selected: number[];
  setSelected: React.Dispatch<React.SetStateAction<number[]>>;
  color: string;
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  loading: boolean;
}> = ({ data, title, selected, setSelected, color, page, totalPages, onPageChange, loading }) => (
  <div className="invoices-table">
    <h3>{title}</h3>
    <table className="invoice-table">
      <thead>
        <tr>
          <th className="invoice-th"></th>
          <th className="invoice-th">Дата поступления</th>
          <th className="invoice-th">Дата предыдущие</th>
          <th className="invoice-th">Дата накладной</th>
          <th className="invoice-th">Накладная №</th>
          <th className="invoice-th">Груз</th>
          <th className="invoice-th">Отправитель</th>
          <th className="invoice-th">Получатель</th>
          <th className="invoice-th">Откуда</th>
          <th className="invoice-th">Куда</th>
          <th className="invoice-th">Упаковка</th>
          <th className="invoice-th">Описание</th>
          <th className="invoice-th">Цена</th>
          <th className="invoice-th">Склад откуда</th>
          <th className="invoice-th">Склад куда</th>
        </tr>
      </thead>
      <tbody>
        {data.map((row: any) => {
          const s = row.idsending || row.sending;
          return (
            <tr key={row.id} className={selected.includes(row.id) ? "invoice-row-selected" : ""}>
              <td className="invoice-td">
                <input type="checkbox" checked={selected.includes(row.id)} onChange={e => {
                  setSelected(prev => e.target.checked ? [...prev, row.id] : prev.filter((id) => id !== row.id));
                }} />
              </td>
              <td className="invoice-td">{s?.issueDate ? new Date(s.issueDate).toLocaleString() : "-"}</td>
              <td className="invoice-td">{s?.lastNotification ? new Date(s.lastNotification).toLocaleString() : "-"}</td>
              <td className="invoice-td">{s?.sendDate ? new Date(s.sendDate).toLocaleString() : "-"}</td>
              <td className="invoice-td">{s?.docNum ?? "-"}</td>
              <td className="invoice-td">{s?.cargoName ?? "-"}</td>
              <td className="invoice-td">{s?.clientFrom?.title ?? "-"}</td>
              <td className="invoice-td">{s?.clientTo?.title ?? "-"}</td>
              <td className="invoice-td">{s?.cityFrom?.title ?? "-"}</td>
              <td className="invoice-td">{s?.cityTo?.title ?? "-"}</td>
              <td className="invoice-td">{s?.packaging ?? "-"}</td>
              <td className="invoice-td">{s?.description ?? "-"}</td>
              <td className="invoice-td">{s?.price?.priceVolume ?? "-"}</td>
              <td className="invoice-td">{s?.wareFrom?.title ?? "-"}</td>
              <td className="invoice-td">{s?.wareTo?.title ?? "-"}</td>
            </tr>
          );
        })}
      </tbody>
    </table>
    <div className="invoice-pagination">
      <button disabled={page === 0 || loading} onClick={() => onPageChange(page - 1)}>Назад</button>
      <span style={{ margin: '0 8px' }}>Стр. {page + 1} из {totalPages}</span>
      <button disabled={page + 1 >= totalPages || loading} onClick={() => onPageChange(page + 1)}>Вперед</button>
    </div>
  </div>
);

export default function TripCreatePage() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [cities, setCities] = useState<CityDto[]>([]);
  const [tripTypes, setTripTypes] = useState<TripTypeDto[]>([]);
  const [warehousesFrom, setWarehousesFrom] = useState<WarehouseDto[]>([]);
  const [warehousesTo, setWarehousesTo] = useState<WarehouseDto[]>([]);
  const [modalOpen, setModalOpen] = useState(true);

  // Выборы пользователя
  const [cityFromId, setCityFromId] = useState<number | null>(null);
  const [cityToId, setCityToId] = useState<number | null>(null);
  const [warehouseFromId, setWarehouseFromId] = useState<number | null>(null);
  const [warehouseToId, setWarehouseToId] = useState<number | null>(null);
  const [tripTypeId, setTripTypeId] = useState<number | null>(null);

  // Данные формы
  const [dispatchDate, setDispatchDate] = useState("");
  const [arrivalDate, setArrivalDate] = useState("");
  const [expectedDate, setExpectedDate] = useState("");
  const [costs, setCosts] = useState("");
  const [isCityCosts, setIsCityCosts] = useState(0);
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [transports, setTransports] = useState<TransportDto[]>([]);
  const [selectedTransport, setSelectedTransport] = useState<TransportDto | null>(null);
  const [transportCityFilter, setTransportCityFilter] = useState("");

  const [cargoSeal, setCargoSeal] = useState("");

  const [showSuccess, setShowSuccess] = useState(false);

  const [inTripInvoices, setInTripInvoices] = useState<SendingMovingDto[]>([]);
  const [inWarehouseInvoices, setInWarehouseInvoices] = useState<SendingMovingDto[]>([]);
  const [selectedInTrip, setSelectedInTrip] = useState<number[]>([]);
  const [selectedInWarehouse, setSelectedInWarehouse] = useState<number[]>([]);
  const [tripId, setTripId] = useState<number | null>(null);
  const [invoiceLoading, setInvoiceLoading] = useState(false);
  const [invoiceError, setInvoiceError] = useState<string | null>(null);

  // Получение справочников
  useEffect(() => {
    axios.get(`http://localhost:8080/cities`, { headers: { Authorization: `Bearer ${token}` } })
      .then(r => setCities(Array.isArray(r.data) ? r.data as CityDto[] : []));
    axios.get(`http://localhost:8080/trip-types`, { headers: { Authorization: `Bearer ${token}` } })
      .then(r => setTripTypes(Array.isArray(r.data) ? r.data as TripTypeDto[] : []));
    if (!token) return;
    fetchTransports(token)
      .then(data => setTransports(Array.isArray(data) ? data : []));
  }, [token]);

  // Склады по выбранному городу
  useEffect(() => {
    if (cityFromId)
      axios.get(`http://localhost:8080/warehouses?cityId=${cityFromId}`, { headers: { Authorization: `Bearer ${token}` } })
        .then(r => setWarehousesFrom(Array.isArray(r.data) ? r.data as WarehouseDto[] : []));
    else setWarehousesFrom([]);
  }, [cityFromId, token]);
  useEffect(() => {
    if (cityToId)
      axios.get(`http://localhost:8080/warehouses?cityId=${cityToId}`, { headers: { Authorization: `Bearer ${token}` } })
        .then(r => setWarehousesTo(Array.isArray(r.data) ? r.data as WarehouseDto[] : []));
    else setWarehousesTo([]);
  }, [cityToId, token]);

  // Открыть модалку для смены направления
  const openDirectionModal = () => setModalOpen(true);

  // Подтвердить выбор направления
  const handleDirectionOk = () => {
    if (!cityFromId || !cityToId || !warehouseFromId || !warehouseToId || !tripTypeId) {
      setError("Заполните все поля направления");
      return;
    }
    setModalOpen(false);
    setError(null);
  };

  // Отмена — возврат на список
  const handleDirectionCancel = () => navigate("/trips");

  // Новый обработчик стоимости (строгая валидация, как в TripUnion)
  const handleCostsChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value.replace(',', '.');
    // Удаляем все, кроме цифр и точки
    value = value.replace(/[^\d.]/g, '');
    // Оставляем только одну точку
    const parts = value.split('.');
    if (parts.length > 2) {
      value = parts[0] + '.' + parts.slice(1).join('');
    }
    // Ограничиваем до двух знаков после точки
    if (value.includes('.')) {
      const [intPart, decPart] = value.split('.');
      value = intPart + '.' + (decPart ? decPart.slice(0, 2) : '');
    }
    setCosts(value);
  };

  // Фильтрация и сортировка транспорта по городу
  const filteredTransports = transports
    .filter(t =>
      !transportCityFilter || t.cityTitle === cities.find(c => c.id.toString() === transportCityFilter)?.title
    )
    .slice().sort((a, b) => (a.model || "").localeCompare(b.model || "", 'ru'));

  // Форматирование даты для бэкенда
  function formatDateForBackend(dateStr: string) {
    if (!dateStr) return null;
    const date = new Date(dateStr);
    const pad = (n: number, z = 2) => ('00' + n).slice(-z);
    return (
      date.getFullYear() + '-' +
      pad(date.getMonth() + 1) + '-' +
      pad(date.getDate()) + ' ' +
      pad(date.getHours()) + ':' +
      pad(date.getMinutes()) + ':' +
      pad(date.getSeconds()) + '.' +
      pad(date.getMilliseconds(), 3)
    );
  }

  // Функции для загрузки накладных
  const fetchInvoices = async (tripId: number, warehouseId: number) => {
    setInvoiceLoading(true);
    setInvoiceError(null);
    try {
      const [tripRes, warehouseRes] = await Promise.all([
        axios.get<SendingMovingDto[]>(`http://localhost:8080/sending-movings/trips/${tripId}`, { headers: { Authorization: `Bearer ${token}` } }),
        axios.get<SendingMovingDto[]>(`http://localhost:8080/sending-movings/warehouses/${warehouseId}`, { headers: { Authorization: `Bearer ${token}` } })
      ]);
      setInTripInvoices(tripRes.data);
      setInWarehouseInvoices(warehouseRes.data);
    } catch (e: any) {
      setInvoiceError(e?.response?.data?.message || 'Ошибка загрузки накладных');
    } finally {
      setInvoiceLoading(false);
    }
  };

  // Отправка формы создания рейса
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const response = await axios.post<{ id: number }>(`http://localhost:8080/trips`, {
        cityFromId, cityToId, warehouseFromId, warehouseToId, tripTypeId,
        dispatchDate: formatDateForBackend(dispatchDate),
        arrivalDate: formatDateForBackend(arrivalDate),
        expectedDate: formatDateForBackend(expectedDate),
        costs: costs ? Number(costs) : null,
        isCityCosts,
        description,
        isTransit: 0,
        state: "0",
        transportId: selectedTransport ? selectedTransport.id : null,
        cargoSeal: cargoSeal || null
      }, { headers: { Authorization: `Bearer ${token}` } });
      setShowSuccess(true);
      setTripId(response.data.id); // сохраняем id созданного рейса
    } catch (err: any) {
      setError(err?.response?.data?.message || "Ошибка создания рейса");
    } finally {
      setLoading(false);
    }
  };

  // Сброс формы для создания нового рейса
  const handleCreateAnother = () => {
    setCityFromId(null);
    setCityToId(null);
    setWarehouseFromId(null);
    setWarehouseToId(null);
    setTripTypeId(null);
    setDispatchDate("");
    setArrivalDate("");
    setExpectedDate("");
    setCosts("");
    setIsCityCosts(0);
    setDescription("");
    setSelectedTransport(null);
    setTransportCityFilter("");
    setCargoSeal("");
    setShowSuccess(false);
    setModalOpen(true);
  };

  // Обработчики перемещения накладных
  const moveToTrip = async () => {
    if (!tripId || selectedInWarehouse.length === 0) return;
    setInvoiceLoading(true);
    try {
      await axios.put(`http://localhost:8080/sending-movings/move-to-trip/${tripId}`, selectedInWarehouse, { headers: { Authorization: `Bearer ${token}` } });
      await fetchInvoices(tripId, warehouseFromId!);
      setSelectedInWarehouse([]);
    } catch (e: any) {
      setInvoiceError(e?.response?.data?.message || 'Ошибка перемещения');
    } finally {
      setInvoiceLoading(false);
    }
  };
  const moveToWarehouse = async () => {
    if (!tripId || selectedInTrip.length === 0 || !warehouseFromId) return;
    setInvoiceLoading(true);
    try {
      await axios.put(`http://localhost:8080/sending-movings/move-to-warehouse/${warehouseFromId}`, selectedInTrip, { headers: { Authorization: `Bearer ${token}` } });
      await fetchInvoices(tripId, warehouseFromId);
      setSelectedInTrip([]);
    } catch (e: any) {
      setInvoiceError(e?.response?.data?.message || 'Ошибка перемещения');
    } finally {
      setInvoiceLoading(false);
    }
  };
  const moveAllToTrip = async () => {
    if (!tripId || !warehouseFromId) return;
    setInvoiceLoading(true);
    try {
      await axios.put(`http://localhost:8080/sending-movings/move-all-from-warehouse/${warehouseFromId}/to-trip/${tripId}`, {}, { headers: { Authorization: `Bearer ${token}` } });
      await fetchInvoices(tripId, warehouseFromId);
      setSelectedInWarehouse([]);
    } catch (e: any) {
      setInvoiceError(e?.response?.data?.message || 'Ошибка перемещения');
    } finally {
      setInvoiceLoading(false);
    }
  };
  const moveAllToWarehouse = async () => {
    if (!tripId || !warehouseFromId) return;
    setInvoiceLoading(true);
    try {
      await axios.put(`http://localhost:8080/sending-movings/move-all-from-trip/${tripId}/to-warehouse/${warehouseFromId}`, {}, { headers: { Authorization: `Bearer ${token}` } });
      await fetchInvoices(tripId, warehouseFromId);
      setSelectedInTrip([]);
    } catch (e: any) {
      setInvoiceError(e?.response?.data?.message || 'Ошибка перемещения');
    } finally {
      setInvoiceLoading(false);
    }
  };

  // После успешного создания рейса сохраняем tripId и загружаем накладные
  useEffect(() => {
    if (showSuccess && tripId && warehouseFromId) {
      fetchInvoices(tripId, warehouseFromId);
    }
  }, [showSuccess, tripId, warehouseFromId]);

  return (
    <div className="trip-create-root">
      {modalOpen && (
        <div className="modal-backdrop">
          <div className="modal-window">
            <h2>Выбор направления</h2>
            <div className="modal-row">
              <div>
                <label>Откуда:</label>
                <select value={cityFromId ?? ""} onChange={e => setCityFromId(Number(e.target.value))}>
                  <option value="">Выберите город</option>
                  {cities.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map(c => <option key={c.id} value={c.id}>{c.title}</option>)}
                </select>
                <div>Склады:</div>
                <select value={warehouseFromId ?? ""} onChange={e => setWarehouseFromId(Number(e.target.value))}>
                  <option value="">Выберите склад</option>
                  {warehousesFrom.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map(w => <option key={w.id} value={w.id}>{w.title}</option>)}
                </select>
              </div>
              <div>
                <label>Куда:</label>
                <select value={cityToId ?? ""} onChange={e => setCityToId(Number(e.target.value))}>
                  <option value="">Выберите город</option>
                  {cities.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map(c => <option key={c.id} value={c.id}>{c.title}</option>)}
                </select>
                <div>Склады:</div>
                <select value={warehouseToId ?? ""} onChange={e => setWarehouseToId(Number(e.target.value))}>
                  <option value="">Выберите склад</option>
                  {warehousesTo.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map(w => <option key={w.id} value={w.id}>{w.title}</option>)}
                </select>
              </div>
            </div>
            <div className="modal-row">
              <label>Тип рейса:</label>
              <select value={tripTypeId ?? ""} onChange={e => setTripTypeId(Number(e.target.value))}>
                <option value="">Выберите тип</option>
                {tripTypes.map(t => <option key={t.id} value={t.id}>{t.title}</option>)}
              </select>
            </div>
            {error && <div className="error-message">{error}</div>}
            <div className="modal-actions">
              <button onClick={handleDirectionOk}>ОК</button>
              <button onClick={handleDirectionCancel}>Отмена</button>
            </div>
          </div>
        </div>
      )}
      {!modalOpen && (
        <form className="trip-create-form" onSubmit={handleSubmit}>
          <div className="form-row">
            <span>Откуда: {cities.find(c => c.id === cityFromId)?.title} / {warehousesFrom.find(w => w.id === warehouseFromId)?.title}</span>
            <span>Куда: {cities.find(c => c.id === cityToId)?.title} / {warehousesTo.find(w => w.id === warehouseToId)?.title}</span>
            <span>Тип рейса: {tripTypes.find(t => t.id === tripTypeId)?.title}</span>
            <button type="button" onClick={openDirectionModal}>Направление</button>
          </div>
          <div className="form-row">
            <label>Дата отправки:</label>
            <input type="datetime-local" value={dispatchDate} onChange={e => setDispatchDate(e.target.value)} />
            <label>Дата прибытия:</label>
            <input type="datetime-local" value={arrivalDate} onChange={e => setArrivalDate(e.target.value)} />
            <label>Ожидаемая дата прибытия:</label>
            <input type="datetime-local" value={expectedDate} onChange={e => setExpectedDate(e.target.value)} />
          </div>
          <div className="form-row">
            <label>Стоимость:</label>
            <input type="text" value={costs} onChange={handleCostsChange} />
            <label>Кто оплачивает:</label>
            <select value={isCityCosts} onChange={e => setIsCityCosts(Number(e.target.value))}>
              <option value={0}>Получатель</option>
              <option value={1}>Отправитель</option>
            </select>
          </div>
          <div className="form-row">
            <label>Транспорт:</label>
            <div className="form-row">
              <select
                value={transportCityFilter}
                onChange={e => setTransportCityFilter(e.target.value)}
                className="select"
                style={{ minWidth: 200 }}
              >
                <option value="">Все города</option>
                {cities.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map(c => (
                  <option key={c.id} value={c.id}>{c.title}</option>
                ))}
              </select>
            </div>
          </div>
          <div className="form-row">
            <select
              value={selectedTransport?.id ?? ""}
              onChange={e => {
                const t = filteredTransports.find(tr => tr.id === Number(e.target.value));
                setSelectedTransport(t || null);
              }}
              className="select"
              style={{ minWidth: 400 }}
            >
              <option value="">Выберите транспорт</option>
              {filteredTransports.map(t => (
                <option key={t.id} value={t.id}>
                  {t.model} ({t.licensePlate}){t.cityTitle ? ` — ${t.cityTitle}` : ""}
                </option>
              ))}
            </select>
          </div>
          <div className="form-row">
            <label>Описание:</label>
            <textarea value={description} onChange={e => setDescription(e.target.value)} />
          </div>
          <div className="form-row">
            <label>Пломба:</label>
            <input type="text" value={cargoSeal} onChange={e => setCargoSeal(e.target.value)} />
          </div>
          {error && <div className="error-message">{error}</div>}
          <div className="form-row">
            <button type="submit" disabled={loading}>{loading ? "Создание..." : "Создать рейс"}</button>
            <button type="button" onClick={() => navigate("/trips")}>Отмена</button>
          </div>
        </form>
      )}
      {showSuccess && tripId && (
        <div className="modal-success">
          <div className="modal-success-content">
            <h3>Рейс успешно создан!</h3>
            <div className="modal-success-actions">
              <button onClick={() => navigate('/trips')}>Вернуться к списку рейсов</button>
              <button onClick={() => navigate(`/trips/edit/${tripId}`)}>Редактировать рейс</button>
            </div>
          </div>
        </div>
      )}
      {!showSuccess && tripId && warehouseFromId && (
        <div className="invoices-block">
          <h2>Работа с накладными</h2>
          {invoiceError && <div className="error-message">{invoiceError}</div>}
          <div className="invoices-tables-column">
            <InvoiceTable
              data={inWarehouseInvoices}
              title="На складе"
              selected={selectedInWarehouse}
              setSelected={setSelectedInWarehouse}
              color="#ffeeba"
              page={warehousePage}
              totalPages={warehouseTotalPages}
              onPageChange={setWarehousePage}
              loading={invoiceLoading}
            />
            <InvoiceTable
              data={inTripInvoices}
              title="В рейсе"
              selected={selectedInTrip}
              setSelected={setSelectedInTrip}
              color="#c3e6cb"
              page={tripPage}
              totalPages={tripTotalPages}
              onPageChange={setTripPage}
              loading={invoiceLoading}
            />
          </div>
          <div className="invoices-actions">
            <button onClick={moveAllToTrip} disabled={invoiceLoading || inWarehouseInvoices.length === 0}>В рейс все</button>
            <button onClick={moveAllToWarehouse} disabled={invoiceLoading || inTripInvoices.length === 0}>На склад все</button>
          </div>
        </div>
      )}
    </div>
  );
} 
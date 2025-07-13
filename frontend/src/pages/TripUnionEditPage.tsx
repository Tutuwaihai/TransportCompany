import { useEffect, useState, useMemo } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../context/useAuth";
import {
  fetchRoutes,
  fetchRoutesPaged,
  fetchDrivers,
  fetchDriversPaged,
  fetchTransports,
  fetchTransportsPaged,
  fetchFirms,
  fetchTrailers,
  fetchCities,
} from "../api/dictionariesApi";
import type { Route, Employee, Transport, Firm, Trailer, City } from "../api/dictionariesApi";
import "./TripUnionCreatePage.css";

export default function TripUnionEditPage() {
  const { id } = useParams();
  const { token } = useAuth();
  const navigate = useNavigate();
  const [routes, setRoutes] = useState<Route[]>([]);
  const [routesPage, setRoutesPage] = useState(0);
  const [routesTotalPages, setRoutesTotalPages] = useState(1);
  const [routesLoading, setRoutesLoading] = useState(false);
  const [drivers, setDrivers] = useState<Employee[]>([]);
  const [driversPage, setDriversPage] = useState(0);
  const [driversTotalPages, setDriversTotalPages] = useState(1);
  const [driversLoading, setDriversLoading] = useState(false);
  const [transports, setTransports] = useState<Transport[]>([]);
  const [transportsLoading, setTransportsLoading] = useState(false);
  const [firms, setFirms] = useState<Firm[]>([]);
  const [cities, setCities] = useState<City[]>([]);
  const [trailers, setTrailers] = useState<Trailer[]>([]);
  const [selectedTrailer, setSelectedTrailer] = useState<Trailer | null>(null);
  const [trailersLoading, setTrailersLoading] = useState(false);

  // Фильтры
  const [fromCityId, setFromCityId] = useState<string>("");
  const [toCityId, setToCityId] = useState<string>("");
  const [selectedRoute, setSelectedRoute] = useState<Route | null>(null);
  const [selectedDriver, setSelectedDriver] = useState<Employee | null>(null);
  const [selectedTransport, setSelectedTransport] = useState<Transport | null>(null);
  const [selectedFirmCustomer, setSelectedFirmCustomer] = useState<Firm | null>(null);
  const [selectedFirmCarrier, setSelectedFirmCarrier] = useState<Firm | null>(null);
  const [costs, setCosts] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [showSuccess, setShowSuccess] = useState(false);

  // Фильтры для водителей
  const [driverFioFilter, setDriverFioFilter] = useState("");
  const [driverCityFilter, setDriverCityFilter] = useState("");
  // Фильтры для транспорта
  const [transportNumberFilter, setTransportNumberFilter] = useState("");
  const [transportCityFilter, setTransportCityFilter] = useState("");
  // Фильтры для прицепов
  const [trailerNumberFilter, setTrailerNumberFilter] = useState("");
  const [trailerCityFilter, setTrailerCityFilter] = useState("");

  // Загрузка данных для редактирования (только по token и id)
  useEffect(() => {
    if (!token || !id) return;
    axios.get(`http://localhost:8080/trip-union/${id}`, { headers: { Authorization: `Bearer ${token}` } })
      .then(res => {
        console.log('Данные для редактирования:', res.data);
        const data = res.data as any;
        setSelectedRoute(data.route || null);
        setSelectedDriver(data.employee || null);
        setSelectedTransport(data.transport || null);
        setSelectedTrailer(data.trailer && data.trailer.id !== -1 ? data.trailer : null);
        setSelectedFirmCustomer(data.firmCustomer || null);
        setSelectedFirmCarrier(data.firmCarrier || null);
        setCosts(data.costs ? String(data.costs) : "");
        setDescription(data.description || "");
      });
  }, [token, id]);

  // Синхронизация выбранных значений после загрузки списков
  useEffect(() => {
    if (selectedRoute && routes.length) {
      const found = routes.find(r => r.id === selectedRoute.id);
      if (found && found !== selectedRoute) setSelectedRoute(found);
    }
  }, [routes.length]);

  useEffect(() => {
    if (selectedDriver && drivers.length) {
      const found = drivers.find(d => d.id === selectedDriver.id);
      if (found && found !== selectedDriver) setSelectedDriver(found);
    }
  }, [drivers.length]);

  useEffect(() => {
    if (selectedTransport && transports.length) {
      const found = transports.find(t => t.id === selectedTransport.id);
      if (found && found !== selectedTransport) setSelectedTransport(found);
    }
  }, [transports.length]);

  useEffect(() => {
    if (selectedTrailer && trailers.length) {
      const found = trailers.find(t => t.id === selectedTrailer.id);
      if (found && found !== selectedTrailer) setSelectedTrailer(found);
    }
  }, [trailers.length]);

  useEffect(() => {
    if (selectedFirmCustomer && firms.length) {
      const found = firms.find(f => f.id === selectedFirmCustomer.id);
      if (found && found !== selectedFirmCustomer) setSelectedFirmCustomer(found);
    }
  }, [firms.length]);

  useEffect(() => {
    if (selectedFirmCarrier && firms.length) {
      const found = firms.find(f => f.id === selectedFirmCarrier.id);
      if (found && found !== selectedFirmCarrier) setSelectedFirmCarrier(found);
    }
  }, [firms.length]);

  useEffect(() => {
    if (!token) return;
    setRoutes([]);
    setRoutesPage(0);
    setRoutesTotalPages(1);
    setRoutesLoading(true);
    fetchRoutesPaged(token, fromCityId, toCityId, 0, 20)
      .then(page => {
        setRoutes(page.content);
        setRoutesTotalPages(page.totalPages);
      })
      .finally(() => setRoutesLoading(false));
    setDrivers([]);
    setDriversPage(0);
    setDriversTotalPages(1);
    setDriversLoading(true);
    fetchDriversPaged(token, 0, 20, driverFioFilter, driverCityFilter, true)
      .then(page => {
        setDrivers(page.content);
        setDriversTotalPages(page.totalPages);
      })
      .finally(() => setDriversLoading(false));
    setTransportsLoading(true);
    fetchTransports(token)
      .then(data => {
        setTransports(data);
      })
      .finally(() => setTransportsLoading(false));
    fetchFirms(token).then(setFirms);
    fetchCities(token).then(setCities);
    setTrailers([]);
    setTrailersLoading(true);
    fetchTrailers(token)
      .then(data => {
        setTrailers(data);
      })
      .finally(() => setTrailersLoading(false));
  }, [token, fromCityId, toCityId, driverFioFilter, driverCityFilter]);

  const handleLoadMoreDrivers = () => {
    if (!token) return;
    setDriversLoading(true);
    fetchDriversPaged(token, driversPage + 1, 20, driverFioFilter, driverCityFilter, true)
      .then(page => {
        setDrivers(prev => [...prev, ...page.content]);
        setDriversPage(page.number);
        setDriversTotalPages(page.totalPages);
      })
      .finally(() => setDriversLoading(false));
  };

  const handleLoadMoreRoutes = () => {
    if (!token) return;
    setRoutesLoading(true);
    fetchRoutesPaged(token, fromCityId, toCityId, routesPage + 1, 20)
      .then(page => {
        setRoutes(prev => [...prev, ...page.content]);
        setRoutesPage(page.number);
        setRoutesTotalPages(page.totalPages);
      })
      .finally(() => setRoutesLoading(false));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await axios.put(
        `http://localhost:8080/trip-union/${id}`,
        {
          idRoute: selectedRoute?.id,
          idEmployee: selectedDriver?.id,
          idTransport: selectedTransport?.id,
          idTrailer: selectedTrailer ? selectedTrailer.id : -1,
          idFirmCustomer: selectedFirmCustomer?.id,
          idFirmCarrier: selectedFirmCarrier?.id,
          costs: costs ? Number(costs) : undefined,
          description,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setShowSuccess(true);
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } };
      setError(error?.response?.data?.message || "Ошибка редактирования путевого листа");
    } finally {
      setLoading(false);
    }
  };

  // Сортировка и фильтрация водителей
  const filteredDrivers = useMemo(() =>
    drivers
      .filter(d =>
        (!driverFioFilter || d.fio.toLowerCase().includes(driverFioFilter.toLowerCase())) &&
        (!driverCityFilter || d.cityTitle === cities.find(c => c.id.toString() === driverCityFilter)?.title)
      )
      .slice().sort((a, b) => a.fio.localeCompare(b.fio, 'ru')),
    [drivers, driverFioFilter, driverCityFilter, cities]
  );
  // Сортировка и фильтрация транспорта
  const filteredTransports = useMemo(() =>
    transports
      .filter(t =>
        (!transportNumberFilter || (t.licensePlate && t.licensePlate.toLowerCase().includes(transportNumberFilter.toLowerCase()))) &&
        (!transportCityFilter || t.cityTitle === cities.find(c => c.id.toString() === transportCityFilter)?.title)
      )
      .slice().sort((a, b) => (a.licensePlate || "").localeCompare(b.licensePlate || "", 'ru')),
    [transports, transportNumberFilter, transportCityFilter, cities]
  );
  // Сортировка и фильтрация прицепов
  const filteredTrailers = useMemo(() =>
    trailers
      .filter(tr =>
        (!trailerNumberFilter || (tr.licensePlate && tr.licensePlate.toLowerCase().includes(trailerNumberFilter.toLowerCase()))) &&
        (!trailerCityFilter || tr.cityTitle === cities.find(c => c.id.toString() === trailerCityFilter)?.title)
      )
      .slice().sort((a, b) => (a.licensePlate || "").localeCompare(b.licensePlate || "", 'ru')),
    [trailers, trailerNumberFilter, trailerCityFilter, cities]
  );

  // Новый обработчик стоимости
  const handleCostsChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value.replace(',', '.');
    value = value.replace(/[^\d.]/g, '');
    const parts = value.split('.');
    if (parts.length > 2) {
      value = parts[0] + '.' + parts.slice(1).join('');
    }
    if (value.includes('.')) {
      const [intPart, decPart] = value.split('.');
      value = intPart + '.' + (decPart ? decPart.slice(0, 2) : '');
    }
    setCosts(value);
  };
  const handleDescriptionChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setDescription(e.target.value);
  };

  // Сброс формы (оставляет текущие значения)
  const handleStay = () => {
    setShowSuccess(false);
  };

  return (
    <div className="trip-create-root">
      <div className="create-header-row">
        <h2 className="create-title">Редактирование путевого листа</h2>
        <button
          type="button"
          className="create-back-btn"
          onClick={() => navigate('/trip-union')}
        >
          ← Вернуться к списку
        </button>
      </div>
      {showSuccess && (
        <div className="modal-success">
          <div className="modal-success-content">
            <h3>Путевой лист успешно обновлён!</h3>
            <div className="modal-success-actions">
              <button onClick={() => navigate('/trip-union')}>Вернуться к списку</button>
              <button onClick={handleStay}>Остаться</button>
            </div>
          </div>
        </div>
      )}
      <form
        onSubmit={handleSubmit}
        className={`create-form${showSuccess ? ' blurred' : ''}`}
      >
        <div className="form-row">
          <div className="form-col">
            <label>Откуда:</label>
            <select value={fromCityId} onChange={e => setFromCityId(e.target.value)} className="select">
              <option value="">Все города</option>
              {cities.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map((c) => (
                <option key={c.id} value={c.id}>{c.title}</option>
              ))}
            </select>
          </div>
          <div className="form-col">
            <label>Куда:</label>
            <select value={toCityId} onChange={e => setToCityId(e.target.value)} className="select">
              <option value="">Все города</option>
              {cities.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map((c) => (
                <option key={c.id} value={c.id}>{c.title}</option>
              ))}
            </select>
          </div>
        </div>
        <div className="form-block">
          <div>
            <label>Фирма заказчик:</label>
            <select
              value={selectedFirmCustomer?.id || ""}
              onChange={e => {
                const firm = firms.find(f => f.id === Number(e.target.value));
                setSelectedFirmCustomer(firm || null);
              }}
              className="select"
            >
              <option value="">Выберите фирму</option>
              {firms.map(f => (
                <option key={f.id} value={f.id}>{f.title}</option>
              ))}
            </select>
          </div>
          <div>
            <label>Фирма перевозчик:</label>
            <select
              value={selectedFirmCarrier?.id || ""}
              onChange={e => {
                const firm = firms.find(f => f.id === Number(e.target.value));
                setSelectedFirmCarrier(firm || null);
              }}
              className="select"
            >
              <option value="">Выберите фирму</option>
              {firms.map(f => (
                <option key={f.id} value={f.id}>{f.title}</option>
              ))}
            </select>
          </div>
        </div>
        <div className="form-row">
          <div className="form-col">
            <label>Маршруты:</label>
            <div className="form-col">
              <div className="table-container">
                <table className="table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Название</th>
                    </tr>
                  </thead>
                  <tbody>
                    {routes.map((r) => (
                      <tr
                        key={r.id}
                        style={{ background: selectedRoute?.id === r.id ? "#e0e0e0" : undefined, cursor: "pointer" }}
                        onClick={() => setSelectedRoute(r)}
                      >
                        <td>{r.id}</td>
                        <td>{r.title}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                {routesPage + 1 < routesTotalPages && (
                  <button type="button" onClick={handleLoadMoreRoutes} disabled={routesLoading} className="load-more-btn">
                    {routesLoading ? "Загрузка..." : "Показать ещё"}
                  </button>
                )}
              </div>
            </div>
          </div>
          <div className="form-col">
            <label>Водители:</label>
            <div className="form-row">
              <input
                type="text"
                placeholder="ФИО водителя"
                value={driverFioFilter}
                onChange={e => setDriverFioFilter(e.target.value)}
                className="input"
              />
              <select
                value={driverCityFilter}
                onChange={e => setDriverCityFilter(e.target.value)}
                className="select"
              >
                <option value="">Все города</option>
                {cities.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map(c => (
                  <option key={c.id} value={c.id}>{c.title}</option>
                ))}
              </select>
            </div>
            <div className="table-container">
              <table className="table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>ФИО</th>
                    <th>Город</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredDrivers.map((d) => (
                    <tr
                      key={d.id}
                      style={{ background: selectedDriver?.id === d.id ? "#e0e0e0" : undefined, cursor: "pointer" }}
                      onClick={() => setSelectedDriver(d)}
                    >
                      <td>{d.id}</td>
                      <td>{d.fio}</td>
                      <td>{d.cityTitle || "-"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {driversPage + 1 < driversTotalPages && (
                <button type="button" onClick={handleLoadMoreDrivers} disabled={driversLoading} className="load-more-btn">
                  {driversLoading ? "Загрузка..." : "Показать ещё"}
                </button>
              )}
            </div>
          </div>
        </div>
        <div className="form-row">
          <div className="form-col">
            <label>Транспорт:</label>
            <div className="form-row">
              <input
                type="text"
                placeholder="Госномер"
                value={transportNumberFilter}
                onChange={e => setTransportNumberFilter(e.target.value)}
                className="input"
              />
              <select
                value={transportCityFilter}
                onChange={e => setTransportCityFilter(e.target.value)}
                className="select"
              >
                <option value="">Все города</option>
                {cities.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map(c => (
                  <option key={c.id} value={c.id}>{c.title}</option>
                ))}
              </select>
            </div>
            <div className="table-container">
              <table className="table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Модель</th>
                    <th>Госномер</th>
                    <th>Тоннаж</th>
                    <th>Город</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredTransports.map((t) => (
                    <tr
                      key={t.id}
                      style={{ background: selectedTransport?.id === t.id ? "#e0e0e0" : undefined, cursor: "pointer" }}
                      onClick={() => setSelectedTransport(t)}
                    >
                      <td>{t.id}</td>
                      <td>{t.model}</td>
                      <td>{t.licensePlate}</td>
                      <td>{t.tonnage}</td>
                      <td>{t.cityTitle}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
          <div className="form-col">
            <label>Прицепы:</label>
            <div className="form-row">
              <input
                type="text"
                placeholder="Модель/номер прицепа"
                value={trailerNumberFilter}
                onChange={e => setTrailerNumberFilter(e.target.value)}
                className="input"
              />
              <select
                value={trailerCityFilter}
                onChange={e => setTrailerCityFilter(e.target.value)}
                className="select"
              >
                <option value="">Все города</option>
                {cities.slice().sort((a, b) => a.title.localeCompare(b.title, 'ru')).map(c => (
                  <option key={c.id} value={c.id}>{c.title}</option>
                ))}
              </select>
            </div>
            <div className="table-container">
              <table className="table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Модель</th>
                    <th>Госномер</th>
                    <th>Тоннаж</th>
                    <th>Город</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredTrailers.map((tr) => (
                    <tr
                      key={tr.id}
                      style={{ background: selectedTrailer?.id === tr.id ? "#e0e0e0" : undefined, cursor: "pointer" }}
                      onClick={() => setSelectedTrailer(tr)}
                    >
                      <td>{tr.id}</td>
                      <td>{tr.model}</td>
                      <td>{tr.licensePlate}</td>
                      <td>{tr.tonnage}</td>
                      <td>{tr.cityTitle || "-"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div className="form-row">
          <div className="form-col">
            <label>Стоимость:</label>
            <input
              type="text"
              value={costs}
              onChange={handleCostsChange}
              className="input"
              placeholder="Введите стоимость"
              inputMode="decimal"
            />
          </div>
          <div className="form-col">
            <label>Описание:</label>
            <textarea
              value={description}
              onChange={handleDescriptionChange}
              className="textarea"
              placeholder="Введите описание"
            />
          </div>
        </div>
        {error && <div className="error-message">{error}</div>}
        <div className="form-row">
          <button type="submit" disabled={loading || !selectedRoute || !selectedDriver || !selectedTransport || !selectedFirmCustomer || !selectedFirmCarrier} className="submit-btn">
            {loading ? "Сохранение..." : "Сохранить изменения"}
          </button>
        </div>
      </form>
    </div>
  );
} 
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../context/useAuth";
import "./TripPage.css";

interface CityDto {
  id: number;
  title: string;
}

interface WarehouseDto {
  id: number;
  title: string;
}

interface TripTypeDto {
  id: number;
  title: string;
}

interface EmployeeDto {
  id: number;
  fio: string;
  cityTitle: string;
}

interface TransportDto {
  id: number;
  model: string;
  licensePlate: string;
  tonnage: number;
  cityTitle: string;
  type: number;
}

interface TripStateDto {
  id: number;
  title: string;
  description?: string | null;
}

interface TripResponse {
  id: number;
  description: string | null;
  cityFrom: CityDto;
  cityTo: CityDto;
  wareHouseFrom: WarehouseDto | null;
  wareHouseTo: WarehouseDto | null;
  tripType: TripTypeDto;
  dispatchDate: string | null;
  arrivalDate: string | null;
  docNum: string | null;
  isTransit: boolean;
  state: TripStateDto | null;
  costs: number;
  transport: TransportDto | null;
  cargoSeal: string | null;
  createDate: string;
}

interface PaginatedResponse {
  content: TripResponse[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export default function TripPage() {
  const [trips, setTrips] = useState<TripResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [startDate, setStartDate] = useState<string>("");
  const [endDate, setEndDate] = useState<string>("");
  const pageSize = 25;
  const { token, logout } = useAuth();
  const navigate = useNavigate();

  const fetchTrips = (page: number) => {
    if (!token) return;

    setLoading(true);
    let url = `http://localhost:8080/trips?page=${page}&size=${pageSize}`;
    
    if (startDate) {
      const isoStart = new Date(startDate).toISOString();
      url += `&startDate=${isoStart}`;
    }
    if (endDate) {
      const isoEnd = new Date(endDate).toISOString();
      url += `&endDate=${isoEnd}`;
    }

    axios
      .get<PaginatedResponse>(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setTrips(res.data.content);
        setTotalPages(res.data.totalPages);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Ошибка загрузки:", err);
        setLoading(false);
        if (err.response?.status === 401) {
          logout();
          navigate("/login");
        }
      });
  };

  useEffect(() => {
    if (token === null) return;

    if (!token) {
      navigate("/login");
      return;
    }

    fetchTrips(currentPage);
  }, [token, currentPage, startDate, endDate, logout, navigate]);

  const handlePageChange = (newPage: number) => {
    setCurrentPage(newPage);
  };

  const handleFilterChange = () => {
    setCurrentPage(0);
    fetchTrips(0);
  };

  const renderPaginationButtons = () => {
    const buttons = [];
    const maxVisiblePages = 5;
    const ellipsis = <span key={`ellipsis-${currentPage}`} className="pagination-ellipsis">...</span>;

    if (totalPages <= 1) return null;

    // Always show first page
    buttons.push(
      <button
        key={0}
        onClick={() => handlePageChange(0)}
        className={`pagination-btn${currentPage === 0 ? ' active' : ''}`}
      >
        1
      </button>
    );

    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(totalPages - 2, startPage + maxVisiblePages - 1);

    if (endPage - startPage < maxVisiblePages - 1) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    if (startPage > 1) {
      buttons.push(ellipsis);
    }

    for (let i = startPage; i <= endPage; i++) {
      buttons.push(
        <button
          key={i}
          onClick={() => handlePageChange(i)}
          className={`pagination-btn${currentPage === i ? ' active' : ''}`}
        >
          {i + 1}
        </button>
      );
    }

    if (endPage < totalPages - 2) {
      buttons.push(ellipsis);
    }

    // Always show last page
    if (totalPages > 1) {
      buttons.push(
        <button
          key={totalPages - 1}
          onClick={() => handlePageChange(totalPages - 1)}
          className={`pagination-btn${currentPage === totalPages - 1 ? ' active' : ''}`}
        >
          {totalPages}
        </button>
      );
    }

    return buttons;
  };

  return (
    <div className="trip-page-root">
      <button className="logout-btn" onClick={logout}>Выйти</button>
      <h1 className="trip-title">Рейсы</h1>
      <div className="filters-block">
        <div className="filters-row">
          <div>
            <label>От даты: </label>
            <input
              type="datetime-local"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </div>
          <div>
            <label>До даты: </label>
            <input
              type="datetime-local"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </div>
          <button onClick={handleFilterChange}>Применить фильтры</button>
          <button onClick={() => {
            setStartDate("");
            setEndDate("");
            handleFilterChange();
          }}>Сбросить фильтры</button>
        </div>
        <button className="create-trip-btn" onClick={() => navigate('/trips/create')}>Создать рейс</button>
      </div>
      <table className="trip-table">
        <thead>
          <tr>
            <th>Дата создания</th>
            <th>Город отправления</th>
            <th>Город прибытия</th>
            <th>Склад отправления</th>
            <th>Склад прибытия</th>
            <th>Тип рейса</th>
            <th>Дата отправления</th>
            <th>Дата прибытия</th>
            <th>Номер документа</th>
            <th>Транзит</th>
            <th>Состояние</th>
            <th>Стоимость</th>
            <th>Транспорт</th>
            <th>Пломба</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {trips.map((trip) => (
            <tr key={trip.id}>
              <td>{new Date(trip.createDate).toLocaleString()}</td>
              <td>{trip.cityFrom.title}</td>
              <td>{trip.cityTo.title}</td>
              <td>{trip.wareHouseFrom?.title ?? "-"}</td>
              <td>{trip.wareHouseTo?.title ?? "-"}</td>
              <td>{trip.tripType.title}</td>
              <td>{trip.dispatchDate ? new Date(trip.dispatchDate).toLocaleString() : "-"}</td>
              <td>{trip.arrivalDate ? new Date(trip.arrivalDate).toLocaleString() : "-"}</td>
              <td>{trip.docNum ?? "-"}</td>
              <td>{trip.isTransit ? "Да" : "Нет"}</td>
              <td>{trip.state?.title ?? "-"}</td>
              <td>{trip.costs.toLocaleString()}</td>
              <td>{trip.transport ? `${trip.transport.model} (${trip.transport.licensePlate})` : "-"}</td>
              <td>{trip.cargoSeal ?? "-"}</td>
              <td>
                <button
                  className="edit-btn"
                  onClick={() => navigate(`/trips/edit/${trip.id}`)}
                >
                  Редактировать
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="pagination-block">
        {renderPaginationButtons()}
      </div>
    </div>
  );
} 
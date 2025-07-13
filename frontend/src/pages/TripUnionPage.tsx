import './TripUnionPage.css';
import { useEffect, useState } from "react";
import axios from "axios";
import Header from "../components/Header";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";

interface FirmDto {
  id: number;
  title: string;
  phone: string | null;
  juraddress: string | null;
}

interface RouteDto {
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

interface TripUnionResponse {
  id: number;
  createDate: string;
  modifyDate: string;
  firmCustomer: FirmDto | null;
  firmCarrier: FirmDto | null;
  route: RouteDto;
  employee: EmployeeDto;
  transport: TransportDto;
  trailer: TransportDto | null;
  costs: number | null;
  description: string | null;
}

interface PaginatedResponse {
  content: TripUnionResponse[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export default function TripUnionPage() {
  const [trips, setTrips] = useState<TripUnionResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [startDate, setStartDate] = useState<string>("");
  const [endDate, setEndDate] = useState<string>("");
  const [filterStartDate, setFilterStartDate] = useState<string>("");
  const [filterEndDate, setFilterEndDate] = useState<string>("");
  const pageSize = 25;
  const { token, logout } = useAuth();
  const navigate = useNavigate();

  const fetchTrips = (page: number) => {
    if (!token) return;

    setLoading(true);
    let url = `http://localhost:8080/trip-union?page=${page}&size=${pageSize}`;
    
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
    setStartDate(filterStartDate);
    setEndDate(filterEndDate);
    setCurrentPage(0);
  };

  const renderPaginationButtons = () => {
    const buttons = [];
    const maxVisiblePages = 5;
    const ellipsis = <span key={`ellipsis-${currentPage}`} className="pagination-ellipsis">...</span>;

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
    const endPage = Math.min(totalPages - 2, startPage + maxVisiblePages - 1);

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

  if (token === null || loading) {
    return <p>Загрузка...</p>;
  }

  return (
    <div className="trip-union-page-root">
      <button className="logout-btn" onClick={logout}>Выйти</button>
      <Header />
      <h1 className="trip-title">Путевые листы</h1>
      <div className="filters-block">
        <div className="filters-row">
          <div>
            <label>От даты: </label>
            <input
              type="datetime-local"
              value={filterStartDate}
              onChange={(e) => setFilterStartDate(e.target.value)}
            />
          </div>
          <div>
            <label>До даты: </label>
            <input
              type="datetime-local"
              value={filterEndDate}
              onChange={(e) => setFilterEndDate(e.target.value)}
            />
          </div>
          <button onClick={handleFilterChange}>Применить фильтры</button>
          <button onClick={() => {
            setFilterStartDate("");
            setFilterEndDate("");
            setStartDate("");
            setEndDate("");
            setCurrentPage(0);
          }}>Сбросить фильтры</button>
        </div>
        <button className="create-trip-btn" onClick={() => navigate('/trip-union/create')}>Создать путевой лист</button>
      </div>
      <table className="trip-table">
        <thead>
          <tr>
            <th>Дата создания</th>
            <th>Клиент</th>
            <th>Перевозчик</th>
            <th>Маршрут</th>
            <th>Сотрудник</th>
            <th>Транспорт</th>
            <th>Прицеп</th>
            <th>Стоимость</th>
            <th>Описание</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {trips.map((trip) => (
            <tr key={trip.id}>
              <td>{new Date(trip.createDate).toLocaleString()}</td>
              <td>{trip.firmCustomer?.title ?? "-"}</td>
              <td>{trip.firmCarrier?.title ?? "-"}</td>
              <td>{trip.route.title}</td>
              <td>{trip.employee.fio}</td>
              <td>{trip.transport.model} ({trip.transport.licensePlate})</td>
              <td>{trip.trailer ? `${trip.trailer.model} (${trip.trailer.licensePlate})` : "нет прицепа"}</td>
              <td>{trip.costs?.toLocaleString() ?? "-"}</td>
              <td>{trip.description ?? "-"}</td>
              <td>
                <button
                  className="edit-btn"
                  onClick={() => navigate(`/trip-union/edit/${trip.id}`)}
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

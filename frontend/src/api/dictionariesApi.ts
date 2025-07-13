import axios from "axios";

const API_URL = "http://localhost:8080";

export interface Route {
  id: number;
  title: string;
}

export interface Employee {
  id: number;
  fio: string;
  cityTitle: string | null;
}

export interface Transport {
  id: number;
  model: string;
  licensePlate: string;
  tonnage: number;
  cityTitle: string;
  modelType?: number;
  ownerType?: number;
}

export interface Trailer {
  id: number;
  model: string;
  licensePlate: string;
  tonnage: number;
  cityTitle: string;
  modelType?: number;
  ownerType?: number;
}

export interface Firm {
  id: number;
  title: string;
  phone?: string | null;
  juraddress?: string | null;
}

export interface City {
  id: number;
  title: string;
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export const fetchRoutes = async (token: string): Promise<Route[]> => {
  const res = await axios.get<Route[]>(`${API_URL}/routes`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
};

export const fetchDrivers = async (token: string): Promise<Employee[]> => {
  const res = await axios.get<Employee[]>(`${API_URL}/drivers`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
};

export const fetchTransports = async (token: string): Promise<Transport[]> => {
  const res = await axios.get<Transport[]>(`${API_URL}/transport`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
};

export const fetchFirms = async (token: string): Promise<Firm[]> => {
  const res = await axios.get<Firm[]>(`${API_URL}/firms`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
};

export const fetchCities = async (token: string): Promise<City[]> => {
  const res = await axios.get<City[]>(`${API_URL}/cities`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
};

export const fetchRoutesPaged = async (
  token: string,
  fromCityId?: string,
  toCityId?: string,
  page: number = 0,
  size: number = 20
): Promise<Page<Route>> => {
  const params = new URLSearchParams();
  if (fromCityId) params.append("fromCityId", fromCityId);
  if (toCityId) params.append("toCityId", toCityId);
  params.append("page", page.toString());
  params.append("size", size.toString());
  const res = await axios.get(`${API_URL}/routes/filter-page?${params.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data as Page<Route>;
};

export const fetchDriversPaged = async (
  token: string,
  page: number = 0,
  size: number = 20,
  fio?: string,
  cityId?: string,
  onlyDrivers: boolean = false
): Promise<Page<Employee>> => {
  const params = new URLSearchParams();
  params.append("page", page.toString());
  params.append("size", size.toString());
  if (fio) params.append("fio", fio);
  if (cityId) params.append("cityId", cityId);
  params.append("onlyDrivers", onlyDrivers.toString());
  const res = await axios.get(`${API_URL}/transport/drivers/page?${params.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data as Page<Employee>;
};

export const fetchTransportsPaged = async (
  token: string,
  page: number = 0,
  size: number = 20
): Promise<Page<Transport>> => {
  const res = await axios.get(`${API_URL}/transport/page?page=${page}&size=${size}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data as Page<Transport>;
};

export const fetchTrailers = async (token: string): Promise<Trailer[]> => {
  const res = await axios.get<Trailer[]>(`${API_URL}/transport/trailers`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
};

export const fetchTrailersPaged = async (
  token: string,
  page: number = 0,
  size: number = 20,
  cityId?: string,
  number?: string
): Promise<Page<Trailer>> => {
  const params = new URLSearchParams();
  params.append("page", page.toString());
  params.append("size", size.toString());
  if (cityId) params.append("cityId", cityId);
  if (number) params.append("number", number);
  const res = await axios.get(`${API_URL}/transport/trailers/page?${params.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data as Page<Trailer>;
};

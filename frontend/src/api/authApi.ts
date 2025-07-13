import axios from 'axios';

export interface ApiResponse<T> {
  code: number;
  status: string;
  message: string;
  data: T;
}

export interface AuthResponse {
  token: string;
  accountId: string;
  type: number;
  isPhoneVerified: boolean;
  isEmailVerified: boolean;
}
const DEV_TOKEN = import.meta.env.VITE_DEV_TOKEN;

export const login = async (email: string, password: string): Promise<AuthResponse> => {
  const response = await axios.post<ApiResponse<AuthResponse>>(
    'http://localhost:8080/auth/login',
    {
      email,
      password,
    },
    {
      headers: {
        "Content-Type": "application/json",
        "NrgApi-DevToken": DEV_TOKEN,
      },
    }
  );

  const authData = response.data.data;

  localStorage.setItem('token', authData.token);

  return authData;
};
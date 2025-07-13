import { useState } from "react";
import { login } from "../api/authApi";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";
import styles from "./LoginPage.module.css";

const LoginPage = () => {
  const { login: saveToken } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const userData = await login(email, password);
      console.log("Вход успешен, получены данные:", userData);
      saveToken(userData.token);
      navigate("/trip-union");
    } catch (err) {
      console.error("Ошибка при входе:", err);
      setError("Неверный логин или пароль");
    }
  };

  return (
    <div className={styles["login-root"]}>
      <div className={styles["login-form-container"]}>
        <h2 className={styles["login-title"]}>Вход в систему</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label className={styles["login-label"]}>Логин:</label><br />
            <input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className={styles["login-input"]}
            />
          </div>
          <div style={{ marginTop: "1rem" }}>
            <label className={styles["login-label"]}>Пароль:</label><br />
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className={styles["login-input"]}
            />
          </div>
          {error && <p className={styles["login-error"]}>{error}</p>}
          <button type="submit" className={styles["login-btn"]}>
            Войти
          </button>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;

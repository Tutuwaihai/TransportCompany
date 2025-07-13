import { useAuth } from "../context/useAuth";

const Header = () => {
  const { token } = useAuth();
  if (!token) return null;
  return null;
};

export default Header;

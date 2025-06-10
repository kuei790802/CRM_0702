import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./index.css";
import App from "./App.jsx";
import Home from "./pages/Home";
import Store from "./pages/Store";
import About from "./pages/About";
import News from "./pages/News";
import Contact from "./pages/Contact";
import Login from "./pages/Login";
import FunnyError from "./components/FunnyError";
import Product from "./pages/Product.jsx";
import NewsDetail from "./pages/NewsDetail.jsx";
import User from "./pages/User.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />, //
    children: [
      { path: "", element: <Home /> },
      { path: "store", element: <Store /> },
      { path: "about", element: <About /> },
      { path: "news", element: <News /> },
      { path: "contact", element: <Contact /> },
      { path: "login", element: <Login /> },
      { path: "Product", element: <Product /> },
      { path: "FunnyError", element: <FunnyError /> },
      {path:"User", element: <User/>},
      {
        path: "/news/:id",
        element: <NewsDetail />,
      },
    ],
  },
]);

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
);

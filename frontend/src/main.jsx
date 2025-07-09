import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import { App as AntdApp } from "antd";
import "./index.css";
import App from "./App";
import router from "./Router.jsx";

// if (import.meta.env.MODE === 'development' || import.meta.env.VITE_USE_MOCK === 'true') {
//   const { worker } = await import('./mocks/browser');
//   await worker.start({
//     onUnhandledRequest: 'bypass',
//   });
// }

const root = createRoot(document.getElementById("root"));

root.render(
  <StrictMode>
    <AntdApp>
      <RouterProvider router={router} />
    </AntdApp>
  </StrictMode>
);

import {
  RobotOutlined,
  UserOutlined,
  AppstoreOutlined,
  BarChartOutlined,
  DesktopOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

const b2cConfig = {
  route: {
    path: "/",
    routes: [
      {
        path: "/b2c/dashboard",
        name: "電商儀表板",
        icon: <RobotOutlined />,
      },
      {
        path: "/b2c/orders",
        name: "訂單管理",
        icon: <RobotOutlined />,
      },
      {
        path: "/b2c/logistics",
        name: "物流配送",
        icon: <RobotOutlined />,
      },
      {
        path: "/b2c/shop",
        name: "商店管理",
        icon: <RobotOutlined />,
      },
      {
        path: "/b2c/report",
        name: "電商報表",
        icon: <RobotOutlined />,
      },
    ],
  },
  location: {
    pathname: "/b2c/dashboard",
  },
};
export default b2cConfig;

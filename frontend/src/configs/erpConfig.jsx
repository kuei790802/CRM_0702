import { AppstoreOutlined ,
  UserOutlined, 
  RobotOutlined, 
  BarChartOutlined, 
  DesktopOutlined
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';


const erpConfig = {
  route: {
    path: '/',
    routes: [
      {
        path: '/erp/dashboard',
        name: '儀表板首頁',
        icon: <AppstoreOutlined />,
      },
      {
        path: '/erp/inventory',
        name: '進銷存管理',
        icon: <AppstoreOutlined />,
      },
      {
        path: '/erp/pricing',
        name: '價格管理',
        icon: <AppstoreOutlined />,
      },
      {
        path: '/erp/report',
        name: '報表管理',
        icon: <AppstoreOutlined />,
      },
      {
        path: '/erp/payment',
        name: '帳款收付',
        icon: <AppstoreOutlined />,
      },
    ],
  },
  location: {
    pathname: '/erp/dashboard',
  },
};
export default erpConfig;

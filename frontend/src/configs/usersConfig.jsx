import { UserOutlined ,
  AppstoreOutlined, 
  RobotOutlined, 
  BarChartOutlined, 
  DesktopOutlined
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';


const usersConfig = {
  route: {
    path: '/',
    routes: [
      {
        path: '/users/management',
        name: '使用者管理',
        icon: <UserOutlined />,
      },
      {
        path: '/users/roles',
        name: '角色權限',
        icon: <UserOutlined />,
      },
      {
        path: '/users/logs',
        name: '操作紀錄',
        icon: <UserOutlined />,
      },
    ],
  },
  location: {
    pathname: '/users/management',
  },
};
export default usersConfig;

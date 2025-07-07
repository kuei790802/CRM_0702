import {
    AppstoreOutlined,
    UserOutlined,
    RobotOutlined,
    BarChartOutlined,
    DesktopOutlined,
    ShoppingCartOutlined,
} from '@ant-design/icons';
// import {useNavigate} from 'react-router-dom';

const erpConfig = {
    route: {
        path: '/',
        routes: [
            {
                path: '/erp/dashboard',
                name: '儀表板',
                icon: <AppstoreOutlined/>,
            },
            {
                path: '/erp/inventory/products',
                name: '產品清單',
                icon: <DesktopOutlined/>,
            },
            {
                path: '/erp/inventory/stocklevels',
                name: '庫存管理',
                icon: <DesktopOutlined/>,
            },
            {
                path: '/erp/sales/orders',
                name: '訂單管理',
                icon: <BarChartOutlined/>,
                routes: [
                    {
                        path: '/erp/sales/orders/:id',
                        name: '訂單明細',
                        hideInMenu: true,
                    },
                    {
                        path: '/erp/sales/orders/new',
                        name: '新增訂單',
                        hideInMenu: true,
                    },
                ],
            },
            {
                path: '/erp/purchaseOrders',
                name: '進貨單管理',
                icon: <ShoppingCartOutlined/>,
                routes: [
                    {
                        path: '/erp/purchaseOrders/:id',
                        name: '進貨單明細',
                        hideInMenu: true,
                    },
                    {
                        path: '/erp/purchaseOrders/new',
                        name: '新增進貨單',
                        hideInMenu: true,
                    },
                ],
            },
            {
                path: '/erp/sales/customers',
                name: '客戶管理',
                icon: <BarChartOutlined/>,
            }
        ],
    },
    location: {
        pathname: '/erp/dashboard',
    },
};

export default erpConfig;
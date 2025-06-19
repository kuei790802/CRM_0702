import React, { useEffect, useState } from 'react';
import { ProList } from '@ant-design/pro-components';
import { Button, Progress, Space, Tag } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from '../../api/axiosInstance';

const ERPPurchaseOrders = () => {
  const [dataSource, setDataSource] = useState([]);
  const [expandedRowKeys, setExpandedRowKeys] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchData = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/erp/purchaseorders');
      setDataSource(res.data.data);
    } catch (error) {
      console.error('進貨單資料抓取失敗:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <ProList
      rowKey="id"
      loading={loading}
      headerTitle="進貨單管理"
      expandable={{
        expandedRowKeys,
        onExpandedRowsChange: setExpandedRowKeys,
      }}
      toolBarRender={() => [
        <Button
          key="new"
          type="primary"
          onClick={() => navigate('/erp/purchaseorder/new')}
        >
          新增進貨單
        </Button>,
      ]}
      dataSource={dataSource}
      metas={{
        title: {
          dataIndex: 'poNumber',
        },
        subTitle: {
          render: (_, row) => (
            <Space>
              <Tag color="blue">{row.supplierName}</Tag>
              <Tag color={getStatusColor(row.status)}>{getStatusText(row.status)}</Tag>
            </Space>
          ),
        },
        description: {
          render: (_, row) =>
            `進貨日期：${row.purchaseDate}，總金額：$${row.totalAmount.toLocaleString()}`,
        },
        content: {
          render: (_, row) => (
            <div style={{ paddingLeft: 24 }}>
              <p style={{ marginBottom: 8, fontWeight: 500 }}>進貨細項：</p>
              <ul style={{ paddingLeft: 16, marginBottom: 0 }}>
                {row.details?.map((item, index) => (
                  <li key={index}>
                    {item.productName} × {item.quantity}（單價 ${item.unitPrice}）
                  </li>
                )) || <li>尚無明細資料</li>}
              </ul>
            </div>
          ),
        },
        actions: {
          render: (_, row) => (
            <a key="edit" onClick={() => navigate(`/erp/purchaseorder/${row.id}`)}>
              編輯
            </a>
          ),
        },
      }}
    />
  );
};

export default ERPPurchaseOrders;

// ✅ 狀態文字與顏色對應
function getStatusText(status) {
  switch (status) {
    case 'draft':
      return '草稿';
    case 'ordered':
      return '已下單';
    case 'received':
      return '已收貨';
    case 'canceled':
      return '已取消';
    default:
      return '未知';
  }
}

function getStatusColor(status) {
  switch (status) {
    case 'draft':
      return 'default';
    case 'ordered':
      return 'orange';
    case 'received':
      return 'green';
    case 'canceled':
      return 'red';
    default:
      return 'gray';
  }
}

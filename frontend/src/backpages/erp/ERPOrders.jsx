import React, { useRef, useState, useEffect } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Button, Pagination, Space, Tag } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from '../../api/axiosInstance';

const PAGE_SIZE = 10;

const ERPOrders = () => {
  const actionRef = useRef();
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchData = async (page = 1) => {
    setLoading(true);
    try {
      const res = await axios.get('/erp/orders', {
        params: { page, pageSize: PAGE_SIZE },
      });
      setData(res.data.data);
      setTotal(res.data.total);
    } catch (error) {
      console.error('訂單資料抓取失敗:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage);
  }, [currentPage]);

  const columns = [
    {
      title: '訂單編號',
      dataIndex: 'orderNo',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '客戶名稱',
      dataIndex: 'customerName',
    },
    {
      title: '狀態',
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: {
        pending: { text: '待處理', status: 'Default' },
        processing: { text: '處理中', status: 'Processing' },
        shipped: { text: '已出貨', status: 'Success' },
        canceled: { text: '已取消', status: 'Error' },
      },
    },
    {
      title: '總金額',
      dataIndex: 'totalAmount',
      render: (_, record) => `$${record.totalAmount.toLocaleString()}`,
    },
    {
      title: '建立日期',
      dataIndex: 'createdAt',
      valueType: 'date',
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (_, record) => [
        <a key="edit" onClick={() => navigate(`/erp/order/${record.id}`)}>編輯</a>,
        <TableDropdown
          key="dropdown"
          menus={[
            { key: 'copy', name: '複製' },
            { key: 'delete', name: '刪除' },
          ]}
        />,
      ],
    },
  ];

  return (
    <div className="bg-white px-2">
      <ProTable
        columns={columns}
        actionRef={actionRef}
        dataSource={data}
        loading={loading}
        rowKey="id"
        search={{ labelWidth: 'auto' }}
        pagination={false}
        dateFormatter="string"
        headerTitle="訂單管理"
        options={false}
        toolBarRender={() => [
          <Button
            key="new"
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => navigate('/erp/order/new')}
          >
            新增訂單
          </Button>,
        ]}
      />

      <div className="flex justify-center py-4">
        <Pagination
          current={currentPage}
          pageSize={PAGE_SIZE}
          total={total}
          onChange={(page) => setCurrentPage(page)}
          showSizeChanger={false}
        />
      </div>
    </div>
  );
};

export default ERPOrders;

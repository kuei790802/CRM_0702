import React, { useRef, useState, useEffect } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Button, Pagination, Space, Tag } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from '../../api/axiosInstance';

const PAGE_SIZE = 10;

const ERPReturns = () => {
  const actionRef = useRef();
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchData = async (page = 1) => {
    setLoading(true);
    try {
      const res = await axios.get('/erp/returns', {
        params: { page, pageSize: PAGE_SIZE },
      });
      setData(res.data.data);
      setTotal(res.data.total);
    } catch (error) {
      console.error('退貨資料抓取失敗:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage);
  }, [currentPage]);

  const columns = [
    {
      title: '退貨編號',
      dataIndex: 'returnNo',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '客戶名稱',
      dataIndex: 'customerName',
    },
    {
      title: '退貨狀態',
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: {
        requested: { text: '申請中', status: 'Default' },
        approved: { text: '已核准', status: 'Processing' },
        refunded: { text: '已退款', status: 'Success' },
        rejected: { text: '已拒絕', status: 'Error' },
      },
    },
    {
      title: '退貨金額',
      dataIndex: 'amount',
      render: (_, record) => `$${record.amount.toLocaleString()}`,
    },
    {
      title: '退貨日期',
      dataIndex: 'returnDate',
      valueType: 'date',
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (_, record) => [
        <a key="edit" onClick={() => navigate(`/erp/return/${record.id}`)}>編輯</a>,
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
        headerTitle="退貨管理"
        options={false}
        toolBarRender={() => [
          <Button
            key="new"
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => navigate('/erp/return/new')}
          >
            新增退貨
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

export default ERPReturns;

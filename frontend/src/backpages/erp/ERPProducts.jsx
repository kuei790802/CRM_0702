import React, { useRef, useState, useEffect } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Button, Pagination, Space, Tag } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from '../../api/axiosInstance';

const PAGE_SIZE = 10;

const ERPProducts = () => {
  const actionRef = useRef();
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchData = async (page = 1) => {
    setLoading(true);
    try {
      const res = await axios.get('/erp/products', {
        params: { page, pageSize: PAGE_SIZE },
      });
      setData(res.data.data);
      setTotal(res.data.total);
    } catch (error) {
      console.error('產品清單抓取失敗:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage);
  }, [currentPage]);

  const columns = [
    {
      title: '產品名稱',
      dataIndex: 'productName',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '產品編號',
      dataIndex: 'sku',
    },
    {
      title: '分類',
      dataIndex: 'category',
    },
    {
      title: '庫存數量',
      dataIndex: 'stock',
      render: (_, record) => (
        <span>{record.stock} 件</span>
      ),
    },
    {
      title: '狀態',
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: {
        active: { text: '上架中', status: 'Success' },
        inactive: { text: '已下架', status: 'Default' },
        lowstock: { text: '庫存不足', status: 'Warning' },
      },
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (_, record) => [
        <a key="edit" onClick={() => navigate(`/erp/product/${record.id}`)}>編輯</a>,
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
        headerTitle="產品清單"
        options={false}
        toolBarRender={() => [
          <Button
            key="new"
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => navigate('/erp/product/new')}
          >
            新增產品
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

export default ERPProducts;

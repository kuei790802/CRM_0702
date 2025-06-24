import React, { useRef, useState, useEffect } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Button, Pagination, Space, Tag } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from '../../api/axiosBackend';

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
    const res = await axios.get('/products', {
      params: {
        page: page - 1,           // 後端從 0 開始
        size: PAGE_SIZE,
        sort: 'productId',        // 根據需要排序的欄位
      },
    });
    setData(res.data.content);     // 注意這邊是 content，不是 data.data
    setTotal(res.data.totalElements); // 總筆數
    console.log('產品清單:', res.data.content);
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
    dataIndex: 'name',
    copyable: true,
    ellipsis: true,
  },
  {
    title: '產品編號',
    dataIndex: 'productCode',
  },
  {
    title: '分類',
    dataIndex: 'categoryName',
  },
  {
    title: '價格',
    dataIndex: 'basePrice',
    render: (price) => <span>${price.toFixed(2)}</span>,
  },
  {
    title: '狀態',
    dataIndex: 'isSalable',
    valueType: 'select',
    valueEnum: {
      true: { text: '上架中', status: 'Success' },
      false: { text: '已下架', status: 'Default' },
    },
  },
  {
    title: '操作',
    valueType: 'option',
    key: 'option',
    render: (_, record) => [
      <a key="edit" onClick={() => navigate(`/erp/product/${record.productId}`)}>編輯</a>,
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

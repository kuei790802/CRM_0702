import React, { useRef, useState, useEffect } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Button, Pagination, Space, Tag } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from '../../api/axiosInstance';

const PAGE_SIZE = 10;

const ERPCustomers = () => {
  const actionRef = useRef();
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchData = async (page = 1) => {
    setLoading(true);
    try {
      const res = await axios.get('/erp/customers', {
        params: { page, pageSize: PAGE_SIZE },
      });
      setData(res.data.data);
      setTotal(res.data.total);
    } catch (error) {
      console.error('客戶資料抓取失敗:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage);
  }, [currentPage]);

  const columns = [
    {
      title: '公司名稱',
      dataIndex: 'companyName',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '聯絡人',
      dataIndex: 'contactPerson',
    },
    {
      title: '電話',
      dataIndex: 'phone',
    },
    {
      title: '所在地',
      dataIndex: 'location',
      ellipsis: true,
    },
    {
      title: '標籤',
      dataIndex: 'tags',
      search: false,
      render: (_, record) => (
        <Space>
          {record.tags.map((tag) => (
            <Tag color="green" key={tag}>{tag}</Tag>
          ))}
        </Space>
      ),
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (_, record) => [
        <a key="edit" onClick={() => navigate(`/erp/customer/${record.id}`)}>編輯</a>,
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
        headerTitle="客戶列表"
        options={false}
        toolBarRender={() => [
          <Button
            key="new"
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => navigate('/erp/customer/new')}
          >
            新增客戶
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

export default ERPCustomers;

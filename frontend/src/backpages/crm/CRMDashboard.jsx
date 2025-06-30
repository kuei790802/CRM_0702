import React, { useEffect, useState } from "react";
import axios from "../../api/axiosBackend";
import FunnelStackedBarChart from "../../backcomponents/crm/FunnelStackedBarChart";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip as RechartsTooltip,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
  FunnelChart,
  Funnel,
  LabelList,
} from "recharts";

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042", "#8884D8"];

const RADIAN = Math.PI / 180;
const renderCustomizedLabel = ({
  cx,
  cy,
  midAngle,
  innerRadius,
  outerRadius,
  percent,
  index,
}) => {
  const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);

  return (
    <text
      x={x}
      y={y}
      fill="#333"
      textAnchor="middle"
      dominantBaseline="central"
      fontSize={12}
    >
      {`${(percent * 100).toFixed(0)}%`}
    </text>
  );
};

const CRMDashboard = () => {
  const [dashboardData, setDashboardData] = useState(null);

  useEffect(() => {
    axios
      .get("/dashboard")
      .then((res) => setDashboardData(res.data))
      .catch((err) => console.error("Dashboard API Error:", err));
  }, []);

  if (!dashboardData) return <div className="text-center p-4">載入中...</div>;

  const { kpis, stageDistribution, monthlyTrend } = dashboardData;

  // 加入 2 張假 KPI 資料
  const extendedKpis = [
    ...kpis,
//     { title: "成交率", value: 25, unit: "%" },
//     { title: "平均成交天數", value: 14, unit: "天" },
  ];

  return (
    <div className="p-6 space-y-8">
      {/* KPI Cards */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        {extendedKpis.map((item, idx) => (
          <div key={idx} className="bg-white shadow-md rounded-xl p-4">
            <p className="text-sm text-gray-500">{item.title}</p>
            <p className="text-2xl font-semibold">
              {item.value}
              {item.unit}
            </p>
          </div>
        ))}
      </div>

      {/* Pie Chart + Funnel 堆疊圖 並排顯示 */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Pie Chart 區塊 */}
        <div className="bg-white shadow-md rounded-xl p-4">
          <h2 className="text-lg font-semibold mb-4">
            {stageDistribution.title}
          </h2>
          <ResponsiveContainer width="100%" height={400}>
            <PieChart>
              <Pie
                data={stageDistribution.data}
                dataKey="value"
                nameKey="name"
                cx="50%"
                cy="50%"
                outerRadius={110}
                labelLine={false}
                label={renderCustomizedLabel}
              >
                {stageDistribution.data.map((entry, index) => (
                  <Cell
                    key={`cell-${index}`}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
              <RechartsTooltip />
              <Legend
                layout="horizontal"
                align="left"
                verticalAlign="bottom"
                iconType="circle"
                formatter={(value) => (
                  <span className="text-sm text-gray-600">{value}</span>
                )}
              />
            </PieChart>
          </ResponsiveContainer>
        </div>

        {/* 堆疊漏斗圖區塊 */}
        <div className="bg-white shadow-md rounded-xl p-4">
          <h2 className="text-lg font-semibold mb-4">商機階段漏斗分析</h2>
          <ResponsiveContainer width="100%" height={250}>
            <FunnelChart>
              <RechartsTooltip />
              <Funnel
                dataKey="value"
                data={stageDistribution.data}
                isAnimationActive
              >
                {stageDistribution.data.map((entry, index) => (
                  <Cell
                    key={`cell-${index}`}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
                <LabelList
                  position="right"
                  fill="#000"
                  stroke="none"
                  dataKey="name"
                />
              </Funnel>
            </FunnelChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Monthly Trend - Line Chart */}
      <div className="bg-white shadow-md rounded-xl p-4">
        <h2 className="text-lg font-semibold mb-4">{monthlyTrend.title}</h2>
        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={monthlyTrend.data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Line
              type="monotone"
              dataKey="value"
              stroke="#8884d8"
              strokeWidth={2}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default CRMDashboard;

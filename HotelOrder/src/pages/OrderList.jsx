import { useState, useEffect } from 'react';
import { SpinnerDotted } from 'spinners-react';
import { FaCalendarAlt, FaHotel, FaUser, FaPhone, FaEnvelope, FaMapMarkerAlt } from 'react-icons/fa';
import { orderApi } from '../services/api';

const OrderList = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // 延迟一点时间再获取数据，确保页面完全加载
    const timer = setTimeout(() => {
      fetchOrders();
    }, 100);
    
    return () => clearTimeout(timer);
  }, []);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // 调试信息
      console.log('orderApi:', orderApi);
      console.log('orderApi.getOrderList:', orderApi.getOrderList);
      
      // 调用API获取订单列表
      const response = await orderApi.getOrderList();
      console.log('API响应:', response);
      
      // 检查响应数据结构
      if (response && response.code === 0 && response.result) {
        // 检查result是否有records字段（分页数据）
        if (response.result.records && Array.isArray(response.result.records)) {
          console.log('分页数据响应:', response.result.records);
          // 映射字段名以匹配前端期望的格式
          const mappedOrders = response.result.records.map(order => ({
            id: order.id,
            orderNumber: order.orderNumber,
            hotelName: order.hotelName,
            roomType: order.roomTypeName,
            checkIn: order.checkInDate,
            checkOut: order.checkOutDate,
            nights: order.nights,
            guests: order.guests,
            totalPrice: order.totalPrice,
            status: order.status,
            customerName: order.customerName,
            customerPhone: order.customerPhone,
            customerEmail: order.customerEmail,
            createTime: order.createTime
          }));
          setOrders(mappedOrders);
        } else if (Array.isArray(response.result)) {
          // 如果result直接是数组
          console.log('直接数组响应:', response.result);
          setOrders(response.result);
        } else {
          console.log('result格式不正确:', response.result);
          throw new Error('数据格式错误');
        }
      } else if (response && Array.isArray(response)) {
        // 如果响应直接是数组
        console.log('直接数组响应:', response);
        setOrders(response);
      } else {
        console.log('响应格式不正确:', response);
        throw new Error('数据格式错误');
      }
    } catch (err) {
      console.error('获取订单列表失败:', err);
      setError(err.message);
      // 如果API调用失败，使用模拟数据
      console.log('使用模拟数据');
      setOrders(getMockOrders());
    } finally {
      setLoading(false);
    }
  };

  // 模拟订单数据
  const getMockOrders = () => [
    {
      id: 1,
      orderNumber: 'ORD202401150001',
      hotelName: '北京锦江之星酒店',
      roomType: '高级客房',
      checkIn: '2024-01-20',
      checkOut: '2024-01-22',
      nights: 2,
      guests: 2,
      totalPrice: 460,
      status: '已确认',
      customerName: '张三',
      customerPhone: '13800138000',
      customerEmail: 'zhangsan@example.com',
      createTime: '2024-01-15 14:30:00'
    },
    {
      id: 2,
      orderNumber: 'ORD202401150002',
      hotelName: '上海锦江之星酒店',
      roomType: '豪华客房',
      checkIn: '2024-01-25',
      checkOut: '2024-01-27',
      nights: 2,
      guests: 1,
      totalPrice: 530,
      status: '待确认',
      customerName: '李四',
      customerPhone: '13900139000',
      customerEmail: 'lisi@example.com',
      createTime: '2024-01-15 16:45:00'
    }
  ];

  const getStatusColor = (status) => {
    switch (status) {
      case '已确认':
        return 'bg-green-100 text-green-800';
      case '待确认':
        return 'bg-yellow-100 text-yellow-800';
      case '已取消':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('zh-CN');
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <SpinnerDotted size={50} thickness={100} speed={100} color="#a37d4c" />
      </div>
    );
  }

  if (error && orders.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">加载失败</h2>
          <p className="text-gray-600 mb-4">{error}</p>
          <button 
            onClick={fetchOrders}
            className="btn btn-primary"
          >
            重新加载
          </button>
        </div>
      </div>
    );
  }

  // 调试信息
  console.log('订单页面状态:', { 
    loading, 
    error, 
    ordersCount: orders ? orders.length : 'orders is null/undefined',
    ordersType: typeof orders,
    isArray: Array.isArray(orders),
    orders: orders
  });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="mb-8">
          <h1 className="text-4xl font-primary mb-4">我的订单</h1>
          <p className="text-lg text-gray-600">查看和管理您的酒店预订订单</p>
          {error && (
            <div className="mt-4 p-3 bg-yellow-100 border border-yellow-400 rounded text-yellow-800">
              <p className="text-sm">API调用失败，显示模拟数据: {error}</p>
            </div>
          )}
        </div>

        {(!orders || !Array.isArray(orders) || orders.length === 0) ? (
          <div className="text-center py-12">
            <div className="text-6xl text-gray-300 mb-4">📋</div>
            <h3 className="text-xl font-medium text-gray-600 mb-2">暂无订单</h3>
            <p className="text-gray-500">您还没有任何预订订单</p>
          </div>
        ) : (
          <div className="space-y-6">
            {(orders && Array.isArray(orders) ? orders : []).map((order) => (
              <div key={order.id} className="bg-white rounded-lg shadow-lg border border-gray-200 overflow-hidden">
                <div className="p-6">
                  {/* 订单头部 */}
                  <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between mb-4">
                    <div className="flex items-center gap-4 mb-2 lg:mb-0">
                      <h3 className="text-xl font-bold text-gray-800">{order.orderNumber}</h3>
                      <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}>
                        {order.status}
                      </span>
                    </div>
                    <div className="text-sm text-gray-500">
                      下单时间：{order.createTime}
                    </div>
                  </div>

                  {/* 酒店信息 */}
                  <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-4">
                    <div className="space-y-3">
                      <h4 className="font-semibold text-gray-800 flex items-center gap-2">
                        <FaHotel className="text-accent" />
                        酒店信息
                      </h4>
                      <div className="pl-6 space-y-2">
                        <p className="text-gray-700">{order.hotelName}</p>
                        <p className="text-gray-600">房型：{order.roomType}</p>
                      </div>
                    </div>

                    <div className="space-y-3">
                      <h4 className="font-semibold text-gray-800 flex items-center gap-2">
                        <FaCalendarAlt className="text-accent" />
                        入住信息
                      </h4>
                      <div className="pl-6 space-y-2">
                        <p className="text-gray-700">
                          入住：{formatDate(order.checkIn)} - 离店：{formatDate(order.checkOut)}
                        </p>
                        <p className="text-gray-600">
                          {order.nights}晚 · {order.guests}人
                        </p>
                      </div>
                    </div>
                  </div>

                  {/* 客户信息 */}
                  <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-4">
                    <div className="space-y-3">
                      <h4 className="font-semibold text-gray-800 flex items-center gap-2">
                        <FaUser className="text-accent" />
                        客户信息
                      </h4>
                      <div className="pl-6 space-y-2">
                        <p className="text-gray-700">{order.customerName}</p>
                        <p className="text-gray-600 flex items-center gap-2">
                          <FaPhone className="text-sm" />
                          {order.customerPhone}
                        </p>
                        <p className="text-gray-600 flex items-center gap-2">
                          <FaEnvelope className="text-sm" />
                          {order.customerEmail}
                        </p>
                      </div>
                    </div>

                    <div className="space-y-3">
                      <h4 className="font-semibold text-gray-800">价格信息</h4>
                      <div className="pl-6">
                        <p className="text-2xl font-bold text-accent">
                          ¥{order.totalPrice}
                        </p>
                        <p className="text-sm text-gray-500">总价</p>
                      </div>
                    </div>
                  </div>

                  {/* 操作按钮 */}
                  <div className="flex flex-col sm:flex-row gap-3 pt-4 border-t border-gray-200">
                    <button className="btn btn-primary flex-1 sm:flex-none">
                      查看详情
                    </button>
                    {order.status === '待确认' && (
                      <button className="btn btn-secondary flex-1 sm:flex-none">
                        取消订单
                      </button>
                    )}
                    {order.status === '已确认' && (
                      <button className="btn btn-secondary flex-1 sm:flex-none">
                        联系客服
                      </button>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </section>
  );
};

export default OrderList;

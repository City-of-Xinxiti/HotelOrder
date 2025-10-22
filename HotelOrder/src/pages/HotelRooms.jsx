import { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { SpinnerDotted } from 'spinners-react';
import { hotelApi, orderApi } from '../services/api';
import { FaArrowLeft, FaCalendarAlt, FaUsers, FaUtensils, FaWindowMaximize, FaBed, FaHome } from 'react-icons/fa';
import { CheckIn, CheckOut, AdultsDropdown, KidsDropdown } from '../components';
import images from '../assets';

const HotelRooms = () => {
  const { hotelId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  
  const [hotel, setHotel] = useState(null);
  const [roomTypes, setRoomTypes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [bookingLoading, setBookingLoading] = useState(false);
  const [bookingSuccess, setBookingSuccess] = useState(false);
  const [roomStatusData, setRoomStatusData] = useState({});
  
  // 筛选条件状态
  const [filters, setFilters] = useState({
    checkIn: '',
    checkOut: '',
    adults: 1,
    children: 0
  });

  useEffect(() => {
    if (hotelId) {
      fetchHotelInfo();
    }
    // 调试图片资源
    console.log('图片资源:', images);
    console.log('Room1Img:', images.Room1Img);
  }, [hotelId]);

  const fetchHotelInfo = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // 从location.state获取酒店信息
      if (location.state?.hotel) {
        setHotel(location.state.hotel);
      }

      // 调用API获取房型信息
      const response = await hotelApi.getHotelRoomTypes(hotelId, 0);
      
      if (response.code === 0 && response.result) {
        // 获取客房图片 (imageType=2)
        try {
          const imageResponse = await hotelApi.getHotelImages(hotelId, 2, 0);
          if (imageResponse.code === 0 && imageResponse.result && imageResponse.result.length > 0) {
            // 为每个房型添加对应的客房图片
            const roomTypesWithImages = response.result.map((roomType) => {
              // 查找该房型对应的图片
              const roomImages = imageResponse.result.filter(img => 
                img.roomTypeCode === roomType.roomTypeCode || img.roomTypeCode === roomType.roomTypeId
              );
              if (roomImages.length > 0) {
                const masterImage = roomImages.find(img => img.master === 1);
                const imageUrl = masterImage ? masterImage.imageUrl : roomImages[0].imageUrl;
                return { ...roomType, image: imageUrl };
              }
              return roomType;
            });
            setRoomTypes(roomTypesWithImages);
          } else {
            setRoomTypes(response.result);
          }
        } catch (imageError) {
          console.warn('获取客房图片失败:', imageError);
          setRoomTypes(response.result);
        }
      } else {
        throw new Error('数据格式错误');
      }
    } catch (err) {
      console.error('获取房型信息失败:', err);
      setError(err.message);
      // 如果API调用失败，使用模拟房型数据
      setRoomTypes([]);
    } finally {
      setLoading(false);
    }
  };

  const handleBackToHotels = () => {
    navigate('/');
  };

  const handleRoomClick = (roomType) => {
    navigate(`/room/${roomType.roomTypeId || roomType.id}`, {
      state: { 
        hotel: hotel,
        roomType: roomType 
      }
    });
  };

  // 处理预订
  const handleBooking = async (roomType) => {
    try {
      setBookingLoading(true);
      setBookingSuccess(false);
      
      // 构建订单数据
      const orderData = {
        hotelId: hotelId || 'H001',
        hotelName: hotelName || '测试酒店',
        roomTypeId: roomType.roomTypeId || roomType.id || 'RT001',
        roomTypeName: roomType.roomTypeName || roomType.name || '标准间',
        checkInDate: filters.checkIn || new Date().toISOString().split('T')[0],
        checkOutDate: filters.checkOut || new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString().split('T')[0],
        nights: 1,
        guests: filters.adults + filters.children,
        totalPrice: getRoomPrice(roomType),
        status: '待确认',
        customerName: '测试用户',
        customerPhone: '13800138000',
        customerEmail: 'test@example.com',
        customerId: 'C' + Date.now()
      };
      
      console.log('提交预订信息:', orderData);
      
      // 调用真实的预订API
      const response = await orderApi.postOrder(orderData);
      
      if (response && response.code === 0) {
        console.log('预订成功:', response);
        setBookingSuccess(true);
        
        // 3秒后隐藏成功提示
        setTimeout(() => {
          setBookingSuccess(false);
        }, 3000);
      } else {
        throw new Error(response?.message || '预订失败');
      }
      
    } catch (err) {
      console.error('预订失败:', err);
      alert('预订失败，请重试: ' + err.message);
    } finally {
      setBookingLoading(false);
    }
  };

  // 获取房态数据
  const fetchRoomStatus = async () => {
    if (!hotelId || !filters.checkIn || !filters.checkOut) {
      return;
    }
    
    try {
      console.log('获取房态数据:', { hotelId, checkIn: filters.checkIn, checkOut: filters.checkOut });
      
      // 计算入住天数
      const checkInDate = new Date(filters.checkIn);
      const checkOutDate = new Date(filters.checkOut);
      const days = Math.ceil((checkOutDate - checkInDate) / (1000 * 60 * 60 * 24));
      
      // 调用房态API
      const response = await hotelApi.getHotelRoomStatus(
        hotelId, 
        filters.checkOut, 
        days, 
        1 // priceType: 1基础协议价, 2追价
      );
      
      if (response && response.code === 0) {
        console.log('房态数据获取成功:', response);
        // 将房态数据转换为以房型ID为key的格式
        const roomStatusMap = {};
        if (response.result && Array.isArray(response.result)) {
          response.result.forEach(roomStatus => {
            if (roomStatus.roomTypeCode) {
              roomStatusMap[roomStatus.roomTypeCode] = roomStatus;
            }
          });
        }
        setRoomStatusData(roomStatusMap);
      } else {
        console.warn('房态数据获取失败:', response?.message);
      }
    } catch (error) {
      console.error('获取房态数据失败:', error);
    }
  };

  // 处理筛选条件变化
  const handleFilterChange = (field, value) => {
    setFilters(prev => ({
      ...prev,
      [field]: value
    }));
    
    // 如果日期发生变化，重新获取房态数据
    if (field === 'checkIn' || field === 'checkOut') {
      fetchRoomStatus();
    }
  };

  // 获取房型的预付价格
  const getRoomPrice = (roomType) => {
    // 如果有房态数据，使用本地预付价格（advanceRate）
    const roomTypeCode = roomType.roomTypeCode || roomType.roomTypeId;
    if (roomStatusData && roomStatusData[roomTypeCode]) {
      const roomStatus = roomStatusData[roomTypeCode];
      return roomStatus.advanceRate || roomStatus.advance_rate || roomType.price || 299;
    }
    
    // 否则使用默认价格
    return roomType.price || 299;
  };

  // 应用筛选
  const applyFilters = () => {
    const totalGuests = filters.adults + filters.children;
    console.log('应用筛选条件:', filters, '总人数:', totalGuests);
    
    // 根据入住人数筛选房型
    const filteredRooms = roomTypes.filter(roomType => {
      const maxOccupancy = roomType.maxOccupancy || roomType.maxPerson || 0;
      return maxOccupancy >= totalGuests;
    });
    
    // 这里可以更新显示的房型列表
    // 目前只是控制台输出，实际项目中可以更新状态
    console.log('筛选后的房型:', filteredRooms);
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center flex flex-col items-center">
          <SpinnerDotted size={50} color="#D4AF37" />
          <p className="mt-4 text-lg">正在加载房型信息...</p>
        </div>
      </div>
    );
  }

  if (error && roomTypes.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center flex flex-col items-center">
          <p className="text-red-500 text-lg">加载失败: {error}</p>
          <button 
            onClick={fetchHotelInfo}
            className="mt-4 px-6 py-2 bg-accent text-white rounded hover:bg-accent/80"
          >
            重试
          </button>
        </div>
      </div>
    );
  }

  return (
    <section>
      {/* 酒店信息头部 */}
      <div className="bg-room h-[400px] relative flex justify-center items-center bg-cover bg-center">
        <div className="absolute w-full h-full bg-black/70" />
        <div className="text-center z-20 text-white">
          <h1 className="text-4xl md:text-6xl font-primary mb-4">
            {hotel ? hotel.innName : '房型选择'}
          </h1>
          {hotel && (
            <p className="text-lg">{hotel.address}</p>
          )}
        </div>
      </div>

      {/* 返回按钮 */}
      <div className="container mx-auto px-4 py-6">
        <button 
          onClick={handleBackToHotels}
          className="flex items-center gap-2 text-accent hover:text-accent/80 transition-colors"
        >
          <FaArrowLeft />
          <span>返回酒店列表</span>
        </button>
      </div>

      {/* 筛选框 */}
      <div className="container mx-auto px-4 py-6">
        <div className="bg-white rounded-lg shadow-lg p-6 border border-gray-200">
          <div className="flex flex-col lg:flex-row gap-4 items-end">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 flex-1">
              {/* 入住日期 */}
              <div className="space-y-2">
                <label className="flex items-center gap-2 text-sm font-medium text-gray-700">
                  <FaCalendarAlt className="text-accent" />
                  入住日期
                </label>
                <div className="h-[48px] border border-gray-300 rounded-md">
                  <CheckIn />
                </div>
              </div>

              {/* 离店日期 */}
              <div className="space-y-2">
                <label className="flex items-center gap-2 text-sm font-medium text-gray-700">
                  <FaCalendarAlt className="text-accent" />
                  离店日期
                </label>
                <div className="h-[48px] border border-gray-300 rounded-md">
                  <CheckOut />
                </div>
              </div>

              {/* 成人数量 */}
              <div className="space-y-2">
                <label className="flex items-center gap-2 text-sm font-medium text-gray-700">
                  <FaUsers className="text-accent" />
                  成人数量
                </label>
                <div className="h-[48px] border border-gray-300 rounded-md">
                  <AdultsDropdown />
                </div>
              </div>

              {/* 儿童数量 */}
              <div className="space-y-2">
                <label className="flex items-center gap-2 text-sm font-medium text-gray-700">
                  <FaUsers className="text-accent" />
                  儿童数量
                </label>
                <div className="h-[48px] border border-gray-300 rounded-md">
                  <KidsDropdown />
                </div>
              </div>
            </div>

            {/* 应用筛选按钮 */}
            <button
              onClick={applyFilters}
              className="bg-accent text-white px-8 py-3 rounded-lg font-medium hover:bg-accent/90 transition-colors duration-200 shadow-lg hover:shadow-xl"
            >
              应用筛选
            </button>
          </div>

          {/* 当前筛选条件显示 */}
          {(filters.checkIn || filters.checkOut || filters.adults > 1 || filters.children > 0) && (
            <div className="mt-4 p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center gap-2 flex-wrap">
                <span className="text-sm text-gray-600">当前筛选条件：</span>
                {filters.checkIn && (
                  <span className="px-2 py-1 bg-accent/20 text-accent text-xs rounded-full">
                    入住: {filters.checkIn}
                  </span>
                )}
                {filters.checkOut && (
                  <span className="px-2 py-1 bg-accent/20 text-accent text-xs rounded-full">
                    离店: {filters.checkOut}
                  </span>
                )}
                <span className="px-2 py-1 bg-accent/20 text-accent text-xs rounded-full">
                  {filters.adults} 成人
                </span>
                {filters.children > 0 && (
                  <span className="px-2 py-1 bg-accent/20 text-accent text-xs rounded-full">
                    {filters.children} 儿童
                  </span>
                )}
              </div>
            </div>
          )}

        </div>
      </div>

          {/* 预订成功提示 */}
          {bookingSuccess && (
            <div className="fixed top-4 right-4 z-50 bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg flex items-center gap-2 animate-bounce">
              <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
              </svg>
              预订成功！
            </div>
          )}

          {/* 房型列表 */}
          <section className="py-24">
            <div className="container mx-auto lg:px-0">

          <div className="space-y-4">
            {roomTypes.map((roomType) => (
              <div 
                key={roomType.roomTypeId || roomType.id} 
                className="bg-white shadow-md rounded-lg overflow-hidden group cursor-pointer hover:shadow-lg transition-shadow duration-300"
                onClick={() => handleRoomClick(roomType)}
              >
                <div className="flex flex-col lg:flex-row">
                  {/* 左侧图片 */}
                  <div className="lg:w-1/4 h-48 lg:h-32">
                    <img 
                      src={roomType.image || images.Room1Img} 
                      alt={roomType.roomTypeName || roomType.name}
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                      onError={(e) => {
                        console.log('图片加载失败:', e.target.src);
                        e.target.src = images.Room1Img;
                      }}
                    />
                  </div>

                  {/* 右侧内容 */}
                  <div className="lg:w-3/4 p-4 flex flex-col justify-between">
                    <div>
                      {/* 房型名称和基本信息 */}
                      <div className="flex items-start justify-between mb-3">
                        <h3 className="text-xl font-bold text-gray-800 flex-1">{roomType.roomTypeName || roomType.name}</h3>
                        <div className="text-2xl font-bold text-accent ml-4">
                          ¥{getRoomPrice(roomType)}
                        </div>
                      </div>
                      
                      {/* 房型特性 */}
                      <div className="mb-3">
                        <div className="flex flex-wrap gap-3 text-sm text-gray-600">
                          <span className="flex items-center gap-1">
                            <FaUtensils className="text-accent text-xs" />
                            含早餐
                          </span>
                          <span className="flex items-center gap-1">
                            <FaBed className="text-accent text-xs" />
                            1.8米大床
                          </span>
                          <span className="flex items-center gap-1">
                            <FaUsers className="text-accent text-xs" />
                            {roomType.maxOccupancy || roomType.maxPerson}人
                          </span>
                          <span className="flex items-center gap-1">
                            <FaHome className="text-accent text-xs" />
                            {roomType.size || 30}㎡
                          </span>
                          <span className="flex items-center gap-1">
                            <FaWindowMaximize className="text-accent text-xs" />
                            有窗户
                          </span>
                        </div>
                      </div>

                      {/* 取消政策 */}
                      <div className="text-xs text-gray-500 mb-3">
                        <span className="font-medium">取消政策：</span>
                        入住前24小时可免费取消
                      </div>
                    </div>

                    {/* 预订按钮 */}
                    <div className="flex justify-end">
                      <button 
                        onClick={() => handleBooking(roomType)}
                        disabled={bookingLoading}
                        className={`px-4 py-2 rounded-md font-medium text-sm transition-colors duration-200 ${
                          bookingLoading 
                            ? 'bg-gray-400 cursor-not-allowed' 
                            : 'bg-accent text-white hover:bg-accent/90'
                        }`}
                      >
                        {bookingLoading ? (
                          <div className="flex items-center gap-2">
                            <div className="w-3 h-3 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                            预订中...
                          </div>
                        ) : (
                          '立即预订'
                        )}
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {roomTypes.length === 0 && !loading && (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">暂无可用房型</p>
            </div>
          )}
        </div>
      </section>
    </section>
  );
};

export default HotelRooms;

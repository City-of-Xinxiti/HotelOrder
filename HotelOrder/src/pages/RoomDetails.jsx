import { AdultsDropdown, CheckIn, CheckOut, KidsDropdown, ScrollToTop } from '../components';
import { useRoomContext } from '../context/RoomContext';
import { hotelRules } from '../constants/data';
import { useParams, useLocation } from 'react-router-dom';
import { FaCheck, FaWifi, FaCoffee, FaBath, FaParking, FaSwimmingPool, FaHotdog, FaStopwatch, FaCocktail, FaTv, FaPhone, FaSnowflake, FaChevronLeft, FaChevronRight } from 'react-icons/fa';
import { BsChevronDown } from 'react-icons/bs';
import { useState, useEffect } from 'react';
import { hotelApi, orderApi } from '../services/api';
import images from '../assets';


const RoomDetails = () => {
  const { id } = useParams(); // id get form url (/room/:id) as string...
  const location = useLocation();
  const { rooms } = useRoomContext();
  
  // 轮播图状态
  const [roomImages, setRoomImages] = useState([]);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [loadingImages, setLoadingImages] = useState(false);
  const [roomStatusData, setRoomStatusData] = useState({});

  // 优先使用传递过来的房型数据，如果没有则使用Context中的静态数据
  const passedRoomType = location.state?.roomType;
  const room = passedRoomType || rooms.find(room => room.id === +id);

  // 获取客房图片 (imageType=2)
  useEffect(() => {
    const fetchRoomImages = async () => {
      if (room?.innId || room?.hotelId) {
        try {
          setLoadingImages(true);
          const hotelId = room.innId || room.hotelId;
          const response = await hotelApi.getHotelImages(hotelId, 2, 0);
          
          if (response.code === 0 && response.result && response.result.length > 0) {
            // 过滤出该房型的图片
            const roomTypeCode = room.roomTypeCode || room.roomTypeId;
            const filteredImages = response.result.filter(img => 
              img.roomTypeCode === roomTypeCode
            );
            setRoomImages(filteredImages.length > 0 ? filteredImages : response.result);
          }
        } catch (error) {
          console.warn('获取客房图片失败:', error);
        } finally {
          setLoadingImages(false);
        }
      }
    };

    fetchRoomImages();
  }, [room]);

  // 轮播图控制函数
  const nextImage = () => {
    setCurrentImageIndex((prev) => (prev + 1) % roomImages.length);
  };

  const prevImage = () => {
    setCurrentImageIndex((prev) => (prev - 1 + roomImages.length) % roomImages.length);
  };

  const goToImage = (index) => {
    setCurrentImageIndex(index);
  };

  // 处理房型数据字段映射
  const roomData = room ? {
    name: room.roomTypeName || room.name,
    description: room.description || room.roomTypeDesc || '',
    facilities: room.facilities || [
      { name: '无线网络', icon: FaWifi },
      { name: '空调', icon: FaSnowflake },
      { name: '电视', icon: FaTv },
      { name: '电话', icon: FaPhone }
    ],
    price: room.price || room.roomPrice || 0,
    imageLg: room.imageLg || room.image || room.roomImage || images.Room1ImgLg,
    size: room.size || room.roomSize || 30,
    maxPerson: room.maxPerson || room.maxOccupancy || 2
  } : {};

  const { name, description, facilities, price, imageLg, size, maxPerson } = roomData;

  // 调试信息
  console.log('RoomDetails - 传递的房型数据:', passedRoomType);
  console.log('RoomDetails - 处理后的房型数据:', roomData);
  console.log('RoomDetails - 酒店信息:', location.state?.hotel);

  // 预订表单状态
  const [bookingData, setBookingData] = useState({
    name: '',
    phone: '',
    email: '',
    roomCount: 1
  });

  // 预订状态
  const [bookingLoading, setBookingLoading] = useState(false);
  const [bookingSuccess, setBookingSuccess] = useState(false);

  // 获取房型的预付价格
  const getRoomPrice = (roomData) => {
    // 如果有房态数据，使用本地预付价格（advanceRate）
    const roomTypeCode = roomData.roomTypeCode || roomData.roomTypeId;
    if (roomStatusData && roomStatusData[roomTypeCode]) {
      const roomStatus = roomStatusData[roomTypeCode];
      return roomStatus.advanceRate || roomStatus.advance_rate || roomData.price || 299;
    }
    
    // 否则使用默认价格
    return roomData.price || 299;
  };

  // 获取房态数据
  const fetchRoomStatus = async () => {
    if (!hotelId || !bookingData.checkIn || !bookingData.checkOut) {
      return;
    }
    
    try {
      console.log('获取房态数据:', { hotelId, checkIn: bookingData.checkIn, checkOut: bookingData.checkOut });
      
      // 计算入住天数
      const checkInDate = new Date(bookingData.checkIn);
      const checkOutDate = new Date(bookingData.checkOut);
      const days = Math.ceil((checkOutDate - checkInDate) / (1000 * 60 * 60 * 24));
      
      // 调用房态API
      const response = await hotelApi.getHotelRoomStatus(
        hotelId, 
        bookingData.checkOut, 
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

  // 处理输入变化
  const handleInputChange = (field, value) => {
    setBookingData(prev => ({
      ...prev,
      [field]: value
    }));
    
    // 如果日期发生变化，重新获取房态数据
    if (field === 'checkIn' || field === 'checkOut') {
      fetchRoomStatus();
    }
  };

  // 处理预订提交
  const handleBookingSubmit = async (e) => {
    e.preventDefault();
    
    try {
      setBookingLoading(true);
      setBookingSuccess(false);
      
      // 构建订单数据
      const orderData = {
        hotelId: hotelId || 'H001',
        hotelName: hotelName || '测试酒店',
        roomTypeId: roomTypeId || 'RT001',
        roomTypeName: name || '标准间',
        checkInDate: bookingData.checkIn || new Date().toISOString().split('T')[0],
        checkOutDate: bookingData.checkOut || new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString().split('T')[0],
        nights: 1,
        guests: bookingData.adults || 1,
        totalPrice: getRoomPrice(roomData),
        status: '待确认',
        customerName: bookingData.name || '',
        customerPhone: bookingData.phone || '',
        customerEmail: bookingData.email || '',
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

  return (
    <section>
      <ScrollToTop />
      
      {/* 预订成功提示 */}
      {bookingSuccess && (
        <div className="fixed top-4 right-4 z-50 bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg flex items-center gap-2 animate-bounce">
          <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
          </svg>
          预订成功！
        </div>
      )}

      <div className='bg-room h-[560px] relative flex justify-center items-center bg-cover bg-center'>
        <div className='absolute w-full h-full bg-black/70' />
        <h1 className='text-6xl text-white z-20 font-primary text-center'>{name} 详情</h1>
      </div>

      <div className='container mx-auto'>
        <div className='flex flex-col lg:flex-row lg:gap-x-8 h-full py-24'>
          {/* ⬅️⬅️⬅️ left side ⬅️⬅️⬅️ */}
          <div className='w-full lg:w-[60%] h-full text-justify'>
            <h2 className='h2'>{name}</h2>
            <p className='mb-8'>{description}</p>
            
            {/* 房型基本信息 */}
            <div className="mb-8 p-4 bg-gray-50 rounded-lg">
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div className="flex items-center gap-2">
                  <span className="text-accent font-medium">房间大小:</span>
                  <span>{size}平方米</span>
                </div>
                <div className="flex items-center gap-2">
                  <span className="text-accent font-medium">入住人数:</span>
                  <span>{maxPerson}人</span>
                </div>
                <div className="flex items-center gap-2">
                  <span className="text-accent font-medium">价格:</span>
                  <span className="text-lg font-bold text-accent">¥{price}/晚</span>
                </div>
              </div>
            </div>
            
            {/* 客房图片轮播图 */}
            <div className="mb-8 relative">
              {loadingImages ? (
                <div className="h-96 bg-gray-200 rounded-lg flex items-center justify-center">
                  <div className="text-gray-500">加载图片中...</div>
                </div>
              ) : roomImages.length > 0 ? (
                <div className="relative">
                  {/* 主图片 */}
                  <div className="relative h-96 rounded-lg overflow-hidden">
                    <img 
                      src={roomImages[currentImageIndex]?.imageUrl || imageLg} 
                      alt={`客房图片 ${currentImageIndex + 1}`}
                      className="w-full h-full object-cover"
                      onError={(e) => {
                        console.log('房型图片加载失败，使用默认图片');
                        e.target.src = images.Room1ImgLg;
                      }}
                    />
                    
                    {/* 左右切换按钮 */}
                    {roomImages.length > 1 && (
                      <>
                        <button
                          onClick={prevImage}
                          className="absolute left-4 top-1/2 transform -translate-y-1/2 bg-black/50 text-white p-2 rounded-full hover:bg-black/70 transition-colors"
                        >
                          <FaChevronLeft />
                        </button>
                        <button
                          onClick={nextImage}
                          className="absolute right-4 top-1/2 transform -translate-y-1/2 bg-black/50 text-white p-2 rounded-full hover:bg-black/70 transition-colors"
                        >
                          <FaChevronRight />
                        </button>
                      </>
                    )}
                  </div>
                  
                  {/* 缩略图导航 */}
                  {roomImages.length > 1 && (
                    <div className="flex gap-2 mt-4 overflow-x-auto">
                      {roomImages.map((image, index) => (
                        <button
                          key={index}
                          onClick={() => goToImage(index)}
                          className={`flex-shrink-0 w-16 h-16 rounded-lg overflow-hidden border-2 transition-colors ${
                            index === currentImageIndex 
                              ? 'border-accent' 
                              : 'border-gray-300 hover:border-gray-400'
                          }`}
                        >
                          <img 
                            src={image.imageUrl} 
                            alt={`缩略图 ${index + 1}`}
                            className="w-full h-full object-cover"
                          />
                        </button>
                      ))}
                    </div>
                  )}
                  
                  {/* 图片计数器 */}
                  {roomImages.length > 1 && (
                    <div className="absolute bottom-4 right-4 bg-black/50 text-white px-3 py-1 rounded-full text-sm">
                      {currentImageIndex + 1} / {roomImages.length}
                    </div>
                  )}
                </div>
              ) : (
                <img 
                  className='mb-8' 
                  src={imageLg} 
                  alt="roomImg"
                  onError={(e) => {
                    console.log('房型图片加载失败，使用默认图片');
                    e.target.src = images.Room1ImgLg;
                  }}
                />
              )}
            </div>

            <div className='mt-12'>
              <h3 className='h3 mb-3'></h3>
              <p className='mb-12'> 这间客房为您提供舒适优雅的住宿体验，配备现代化设施和贴心服务。宽敞的空间设计，让您在旅途中享受家的温馨。我们致力于为您打造难忘的住宿回忆。 </p>

              {/* icons grid */}
              <div className="grid grid-cols-3 gap-6 mb-12">
                {
                  facilities.map((item, index) =>
                    <div key={index} className='flex items-center gap-x-3 flex-1'>
                      <div className='text-3xl text-accent'>{<item.icon />}</div>
                      <div className='text-base'>{item.name}</div>
                    </div>
                  )
                }
              </div>
            </div>
          </div>

          {/* ➡️➡️➡️ right side ➡️➡️➡️ */}
          <div className='w-full lg:w-[40%] h-full'>
            {/* reservation */}
            <div className='py-8 px-6 bg-accent/20 mb-12'>
              <form onSubmit={handleBookingSubmit}>
                <div className='flex flex-col space-y-4 mb-4'>
                  <h3>您的预订</h3>
                  
                  {/* 入住离店日期 */}
                  <div className='h-[60px]'> <CheckIn /> </div>
                  <div className='h-[60px]'> <CheckOut /> </div>
                  

                  {/* 房间数量 */}
                  <div className='h-[60px] bg-white relative'>
                    <select 
                      value={bookingData.roomCount}
                      onChange={(e) => handleInputChange('roomCount', parseInt(e.target.value))}
                      className='w-full h-full bg-white px-8 appearance-none cursor-pointer'
                    >
                      <option value={1}>1 间</option>
                      <option value={2}>2 间</option>
                      <option value={3}>3 间</option>
                      <option value={4}>4 间</option>
                    </select>
                    <div className='absolute right-8 top-1/2 transform -translate-y-1/2 pointer-events-none'>
                      <BsChevronDown className='text-base text-accent' />
                    </div>
                  </div>

                  {/* 姓名 */}
                  <div className='h-[60px] bg-white relative'>
                    <input 
                      type="text"
                      value={bookingData.name}
                      onChange={(e) => handleInputChange('name', e.target.value)}
                      placeholder="请输入您的姓名"
                      className='w-full h-full bg-white px-8'
                      required
                    />
                  </div>

                  {/* 电话 */}
                  <div className='h-[60px] bg-white relative'>
                    <input 
                      type="tel"
                      value={bookingData.phone}
                      onChange={(e) => handleInputChange('phone', e.target.value)}
                      placeholder="请输入您的电话号码"
                      className='w-full h-full bg-white px-8'
                      required
                    />
                  </div>

                  {/* 邮箱 */}
                  <div className='h-[60px] bg-white relative'>
                    <input 
                      type="email"
                      value={bookingData.email}
                      onChange={(e) => handleInputChange('email', e.target.value)}
                      placeholder="请输入您的邮箱地址"
                      className='w-full h-full bg-white px-8'
                      required
                    />
                  </div>
                </div>

                <button 
                  type="submit" 
                  disabled={bookingLoading}
                  className={`btn btn-lg w-full ${
                    bookingLoading 
                      ? 'bg-gray-400 cursor-not-allowed' 
                      : 'btn-primary'
                  }`}
                >
                  {bookingLoading ? (
                    <div className="flex items-center justify-center gap-2">
                      <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                      预订中...
                    </div>
                  ) : (
                    `立即预订 ¥${getRoomPrice(roomData)}`
                  )}
                </button>
              </form>
            </div>

            <div>
              <h3 className='h3'>酒店规则</h3>
              <p className='mb-6 text-justify'>
                为了确保所有客人都能享受舒适愉快的住宿体验，我们制定了以下酒店规则。请仔细阅读并遵守这些规定，感谢您的理解与配合。
              </p>

              <ul className='flex flex-col gap-y-4'>
                {
                  hotelRules.map(({ rules }, idx) =>
                    <li key={idx} className='flex items-center gap-x-4'>
                      <FaCheck className='text-accent' />
                      {rules}
                    </li>
                  )
                }
              </ul>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default RoomDetails;

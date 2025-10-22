import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { SpinnerDotted } from 'spinners-react';
import { FaSearch, FaMapMarkerAlt, FaHotel, FaTimes } from 'react-icons/fa';
import { hotelApi, handleApiError } from '../services/api';
import images from '../assets';

const HotelList = () => {
  const [hotels, setHotels] = useState([]);
  const [filteredHotels, setFilteredHotels] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({
    city: '',
    hotelName: ''
  });
  const navigate = useNavigate();

  useEffect(() => {
    fetchHotels();
  }, []);

  // 筛选逻辑
  useEffect(() => {
    let filtered = hotels;
    
    if (filters.city) {
      filtered = filtered.filter(hotel => 
        hotel.cityName.toLowerCase().includes(filters.city.toLowerCase())
      );
    }
    
    if (filters.hotelName) {
      filtered = filtered.filter(hotel => 
        hotel.innName.toLowerCase().includes(filters.hotelName.toLowerCase())
      );
    }
    
    setFilteredHotels(filtered);
  }, [hotels, filters]);

  const fetchHotels = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // 调用API获取酒店列表
      const response = await hotelApi.getHotelList({
        pageNum: 1,
        pageSize: 20,
        cityCode: '010', // 北京
        languageCode: 0
      });

      if (response.code === 0 && response.result) {
        const hotelsWithImages = await Promise.all(
          response.result.map(async (hotel) => {
            try {
              // 获取酒店图片 (imageType=1)
              const imageResponse = await hotelApi.getHotelImages(hotel.innId, 1, 0);
              if (imageResponse.code === 0 && imageResponse.result && imageResponse.result.length > 0) {
                // 查找主图或第一张图片
                const masterImage = imageResponse.result.find(img => img.master === 1);
                const imageUrl = masterImage ? masterImage.imageUrl : imageResponse.result[0].imageUrl;
                return {
                  ...hotel,
                  image: imageUrl
                };
              }
              return hotel;
            } catch (imageError) {
              console.warn(`获取酒店 ${hotel.innId} 图片失败:`, imageError);
              return hotel;
            }
          })
        );
        setHotels(hotelsWithImages);
      } else {
        throw new Error('数据格式错误');
      }
    } catch (err) {
      console.error('获取酒店列表失败:', err);
      setError(err.message);
      // 如果API调用失败，使用模拟数据
      setHotels([]);
    } finally {
      setLoading(false);
    }
  };

  const handleHotelClick = (hotel) => {
    // 跳转到房型页面，传递酒店信息
    navigate(`/rooms/${hotel.innId}`, { 
      state: { 
        hotel: hotel 
      } 
    });
  };

  const handleFilterChange = (filterType, value) => {
    setFilters(prev => ({
      ...prev,
      [filterType]: value
    }));
  };

  const clearFilters = () => {
    setFilters({
      city: '',
      hotelName: ''
    });
  };

  const getStarDisplay = (starType) => {
    if (starType === -1) return '无星';
    return '★'.repeat(starType + 1);
  };

  const getLevelDisplay = (innLevel) => {
    const levels = ['经济型', '舒适型', '品质型', '高档型', '豪华型'];
    return levels[innLevel] || '未知';
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center flex flex-col items-center">
          <SpinnerDotted size={50} color="#D4AF37" />
          <p className="mt-4 text-lg">正在加载酒店列表...</p>
        </div>
      </div>
    );
  }

  if (error && hotels.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center flex flex-col items-center">
          <p className="text-red-500 text-lg">加载失败: {error}</p>
          <button 
            onClick={fetchHotels}
            className="mt-4 px-6 py-2 bg-accent text-white rounded hover:bg-accent/80"
          >
            重试
          </button>
        </div>
      </div>
    );
  }

  return (
    <section className="py-24">
      {/* 页面头部 */}
      <div className="bg-room h-[400px] relative flex justify-center items-center bg-cover bg-center">
        <div className="absolute w-full h-full bg-black/70" />
        <div className="text-center z-20 text-white">
          <h1 className="text-4xl md:text-6xl font-primary mb-4">欢迎预订酒店</h1>
          <p className="text-lg">请选择您想要预订的酒店</p>
        </div>
      </div>

      <div className="container mx-auto px-4 py-12">
        {/* 筛选组件 */}
        <div className="bg-white rounded-lg shadow-lg p-6 mb-8">
          <div className="flex items-center gap-2 mb-6">
            <FaSearch className="text-accent text-xl" />
            <h2 className="text-2xl font-bold text-gray-800">筛选酒店</h2>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {/* 城市筛选 */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
                <FaMapMarkerAlt className="text-accent" />
                城市
              </label>
              <div className="relative">
                <input
                  type="text"
                  placeholder="请输入城市名称"
                  value={filters.city}
                  onChange={(e) => handleFilterChange('city', e.target.value)}
                  className="w-full h-12 px-3 pr-10 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent focus:border-transparent"
                />
                <div className="absolute right-3 top-1/2 transform -translate-y-1/2">
                  <FaMapMarkerAlt className="text-accent text-sm" />
                </div>
              </div>
            </div>
            
            {/* 酒店名称筛选 */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
                <FaHotel className="text-accent" />
                酒店名称
              </label>
              <div className="relative">
                <input
                  type="text"
                  placeholder="请输入酒店名称"
                  value={filters.hotelName}
                  onChange={(e) => handleFilterChange('hotelName', e.target.value)}
                  className="w-full h-12 px-3 pr-10 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-accent focus:border-transparent"
                />
                <div className="absolute right-3 top-1/2 transform -translate-y-1/2">
                  <FaHotel className="text-accent text-sm" />
                </div>
              </div>
            </div>
            
            {/* 操作按钮 */}
            <div className="flex items-end">
              <button
                onClick={clearFilters}
                className="w-full h-12 px-4 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition-colors duration-200 flex items-center justify-center gap-2"
              >
                <FaTimes />
                清除筛选
              </button>
            </div>
          </div>
          
          {/* 筛选结果统计 */}
          <div className="mt-4 text-sm text-gray-600">
            找到 {filteredHotels.length} 家酒店
            {(filters.city || filters.hotelName) && (
              <span className="ml-2">
                (筛选条件: {filters.city && `城市: ${filters.city}`} 
                {filters.city && filters.hotelName && ', '}
                {filters.hotelName && `酒店: ${filters.hotelName}`})
              </span>
            )}
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {filteredHotels.map((hotel) => (
            <div
              key={hotel.innId}
              onClick={() => handleHotelClick(hotel)}
              className="bg-white rounded-lg shadow-lg hover:shadow-xl transition-all duration-300 cursor-pointer border border-gray-200 hover:border-accent group"
            >
              {/* 酒店图片 */}
              <div className="h-48 bg-gray-200 rounded-t-lg overflow-hidden">
                <img 
                  src={hotel.image || images.Room1Img} 
                  alt={hotel.innName}
                  className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                />
              </div>

              <div className="p-6">
                <div className="flex justify-between items-start mb-4">
                  <h3 className="font-semibold text-xl text-gray-800 line-clamp-2 flex-1">
                    {hotel.innName}
                  </h3>
                  <div className="text-right ml-4">
                    <div className="text-yellow-500 text-lg">
                      {getStarDisplay(hotel.starType)}
                    </div>
                    <div className="text-sm text-gray-500">
                      {getLevelDisplay(hotel.innLevel)}
                    </div>
                  </div>
                </div>
                
                <div className="space-y-2 mb-4">
                  <div className="flex items-center text-gray-600">
                    <svg className="w-4 h-4 mr-2 text-accent" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
                    </svg>
                    <span className="text-sm">{hotel.address}</span>
                  </div>
                  <div className="flex items-center text-gray-600">
                    <svg className="w-4 h-4 mr-2 text-accent" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-8.293l-3-3a1 1 0 00-1.414 0l-3 3a1 1 0 001.414 1.414L9 9.414V13a1 1 0 102 0V9.414l1.293 1.293a1 1 0 001.414-1.414z" clipRule="evenodd" />
                    </svg>
                    <span className="text-sm">{hotel.cityName}</span>
                  </div>
                </div>

                <div className="pt-4 border-t border-gray-200">
                  <button className="w-full bg-accent text-white py-2 px-4 rounded hover:bg-accent/90 transition-colors duration-200">
                    查看房型
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {filteredHotels.length === 0 && !loading && (
          <div className="text-center py-12">
            <div className="text-6xl text-gray-300 mb-4">🏨</div>
            <h3 className="text-xl font-medium text-gray-600 mb-2">
              {hotels.length === 0 ? '暂无可用酒店' : '没有找到符合条件的酒店'}
            </h3>
            <p className="text-gray-500 mb-4">
              {hotels.length === 0 
                ? '请稍后再试或联系客服' 
                : '请尝试调整筛选条件'
              }
            </p>
            {hotels.length > 0 && (
              <button
                onClick={clearFilters}
                className="px-6 py-2 bg-accent text-white rounded-md hover:bg-accent/90 transition-colors duration-200"
              >
                清除筛选
              </button>
            )}
          </div>
        )}
      </div>
    </section>
  );
};

export default HotelList;

import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { SpinnerDotted } from 'spinners-react';
import { hotelApi, handleApiError } from '../services/api';

const HotelSelection = () => {
  const [hotels, setHotels] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchHotels();
  }, []);

  const fetchHotels = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // 调用API获取酒店列表
      const response = await hotelApi.getHotelList({
        pageNum: 1,
        pageSize: 10,
        cityCode: '010', // 北京
        languageCode: 0
      });

      if (response.code === 0 && response.result) {
        setHotels(response.result);
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
        <div className="text-center">
          <SpinnerDotted size={50} color="#D4AF37" />
          <p className="mt-4 text-lg">正在加载酒店列表...</p>
        </div>
      </div>
    );
  }

  if (error && hotels.length === 0) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
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
      <div className="container mx-auto px-4">
        <div className="text-center mb-12">
          <h1 className="font-primary text-4xl md:text-5xl mb-4">选择酒店</h1>
          <p className="text-lg text-gray-600">请选择您想要预订的酒店</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {hotels.map((hotel) => (
            <div
              key={hotel.innId}
              onClick={() => handleHotelClick(hotel)}
              className="bg-white rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-300 cursor-pointer border border-gray-200 hover:border-accent"
            >
              <div className="p-6">
                <div className="flex justify-between items-start mb-4">
                  <h3 className="font-semibold text-xl text-gray-800 line-clamp-2">
                    {hotel.innName}
                  </h3>
                  <div className="text-right">
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
                    <svg className="w-4 h-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
                    </svg>
                    <span className="text-sm">{hotel.address}</span>
                  </div>
                  <div className="flex items-center text-gray-600">
                    <svg className="w-4 h-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
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

        {hotels.length === 0 && !loading && (
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">暂无可用酒店</p>
          </div>
        )}
      </div>
    </section>
  );
};

export default HotelSelection;

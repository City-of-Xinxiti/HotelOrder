import { BookForm, HeroSlider, Rooms, ScrollToTop } from '../components';
import { Link } from 'react-router-dom';


const Home = () => {

  return (
    <div>
      <ScrollToTop />

      <HeroSlider />

      <div className='container mx-auto relative'>

        <div className='bg-accent/20 mt-4 p-4 lg:absolute lg:left-0 lg:right-0 lg:p-0 lg:-top-12 lg:z-30 lg:shadow-xl'>
          <BookForm />
        </div>

      </div>

      <Rooms />

      {/* 酒店选择按钮 */}
      <section className="py-24 bg-gray-50">
        <div className="container mx-auto text-center">
          <h2 className="font-primary text-4xl mb-6">选择您的酒店</h2>
          <p className="text-lg text-gray-600 mb-8">浏览我们精选的酒店，找到最适合您的住宿</p>
          <Link 
            to="/hotels"
            className="btn btn-primary btn-lg"
          >
            浏览酒店列表
          </Link>
        </div>
      </section>
    </div>
  );
};

export default Home;

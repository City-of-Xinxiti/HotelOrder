import { useRoomContext } from '../context/RoomContext';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { LogoWhite } from '../assets'; // PNG Logo
import { LogoDark } from '../assets'; // PNG Logo


const Header = () => {

  const { resetRoomFilterData } = useRoomContext();

  const [header, setHeader] = useState(false);

  useEffect(() => {
    window.addEventListener('scroll', () =>
      window.scrollY > 50
        ? setHeader(true)
        : setHeader(false)
    );
  });

  const navLinks = [
    { name: '首页', path: '/' },
    { name: '客房', path: '/rooms' },
    { name: '餐厅', path: '/restaurant' },
    { name: '水疗', path: '/spa' },
    { name: '订单', path: '/orders' },
    { name: '联系我们', path: '/contact' }
  ];

  return (
    <header
      className={`fixed z-50 w-full transition-all duration-300 
      ${header ? 'bg-white/95 backdrop-blur-sm py-4 shadow-lg' : 'bg-transparent py-6'}`}
    >

      <div className='container mx-auto flex items-center justify-between px-4'>

        {/* Logo */}
        <Link to="/" onClick={resetRoomFilterData} className="flex items-center">
          {
            header
              ? <img className='w-[100px] -mt-4' src={LogoDark} alt="Logo" />
              : <img className='w-[100px] -mt-4' src={LogoWhite} alt="Logo" />
          }
        </Link>

        {/* Navigation */}
        <nav className={`${header ? 'text-accent' : 'text-accent'} 
        hidden md:flex gap-x-8 lg:gap-x-10 font-medium text-sm items-center`}>
          {
            navLinks.map(link =>
              <Link to={link.path} className='transition hover:opacity-80 hover:scale-105' key={link.name}>
                {link.name}
              </Link>
            )
          }
        </nav>

        {/* Mobile Menu Button */}
        <button className={`md:hidden ${header ? 'text-gray-700' : 'text-white'} text-2xl`}>
          ☰
        </button>

      </div>

    </header>
  );
};

export default Header;

import { useRoomContext } from '../context/RoomContext';
import { BsChevronDown } from 'react-icons/bs';
import { kidsList } from '../constants/data';
import { Menu } from '@headlessui/react';

const KidsDropdown = () => {
  const { kids, setKids } = useRoomContext();

  return (
    <Menu as='div' className='w-full h-full bg-white relative'>
      <Menu.Button className='w-full h-full flex items-center justify-between px-3'>
        <span className="text-gray-700">{kids === '0 儿童' ? '无儿童' : kids}</span>
        <BsChevronDown className='text-accent text-base' />
      </Menu.Button>

      <Menu.Items as='ul' className='bg-white absolute w-full flex flex-col z-40 border border-gray-300 rounded-md shadow-lg mt-1'>
        {
          kidsList.map(({ name }, idx) =>
            <Menu.Item
              as='li'
              key={idx}
              onClick={() => setKids(name)}
              className='border-b last-of-type:border-b-0 h-10 hover:bg-accent hover:text-white w-full flex items-center justify-center cursor-pointer px-3'
            >
              {name}
            </Menu.Item>
          )
        }
      </Menu.Items>
    </Menu>
  );
};

export default KidsDropdown;
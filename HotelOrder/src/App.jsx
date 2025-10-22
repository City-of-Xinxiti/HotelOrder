import { BrowserRouter, Route, Routes, } from 'react-router-dom';
import { Footer, Header, PageNotFound, Rooms } from './components';
import { Home, RoomDetails, HotelSelection, HotelList, HotelRooms, OrderList } from './pages';


const App = () => {

  // const paths = [
  //   { path: '/', element: <Home /> },
  //   { path: '/room/:id', element: <RoomDetails /> },
  //   { path: '*', element: <PageNotFound /> },
  // ]

  // const router = createBrowserRouter(paths);
  // <RouterProvider router={router} /> 

  return (

    <main className=''>
      <BrowserRouter>

        <Header />

        <Routes>
          <Route path={'/'} element={<HotelList />} />
          <Route path={'/rooms/:hotelId'} element={<HotelRooms />} />
          <Route path={'/rooms'} element={<Rooms />} />
          <Route path={'/room/:id'} element={<RoomDetails />} />
          <Route path={'/orders'} element={<OrderList />} />
          <Route path={'/restaurant'} element={<div className="py-24 text-center"><h1 className="text-4xl font-primary mb-4">餐厅</h1><p className="text-lg text-gray-600">餐厅功能开发中...</p></div>} />
          <Route path={'/spa'} element={<div className="py-24 text-center"><h1 className="text-4xl font-primary mb-4">水疗</h1><p className="text-lg text-gray-600">水疗功能开发中...</p></div>} />
          <Route path={'/contact'} element={<div className="py-24 text-center"><h1 className="text-4xl font-primary mb-4">联系我们</h1><p className="text-lg text-gray-600">联系我们功能开发中...</p></div>} />
          <Route path={'*'} element={<PageNotFound />} />
        </Routes>

        <Footer />

      </BrowserRouter>
    </main>
  )
}

export default App
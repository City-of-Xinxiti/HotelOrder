// API服务文件，用于管理后端API调用

// 根据环境自动选择API地址
const API_BASE_URL =
  process.env.NODE_ENV === "production"
    ? "/api/hotel"
    : "http://localhost:8082/api/hotel";

// 通用API调用函数
const apiCall = async (endpoint, method = "POST", data = null) => {
  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      method,
      headers: {
        "Content-Type": "application/json",
      },
      body: data ? JSON.stringify(data) : null,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error(`API调用失败 (${endpoint}):`, error);
    throw error;
  }
};

// 酒店相关API
export const hotelApi = {
  // 获取酒店列表
  getHotelList: async (params = {}) => {
    try {
      const queryParams = new URLSearchParams();
      if (params.cityCode) queryParams.append("cityCode", params.cityCode);
      if (params.brandCode) queryParams.append("brandCode", params.brandCode);
      if (params.status) queryParams.append("status", params.status);

      const url = `/hotels${queryParams.toString() ? "?" + queryParams.toString() : ""}`;
      const response = await fetch(`${API_BASE_URL}${url}`);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error("获取酒店列表失败:", error);
      throw error;
    }
  },

  // 获取酒店详细信息
  getHotelInfo: async (innId, languageCode = 0) => {
    try {
      const response = await fetch(`${API_BASE_URL}/hotels/${innId}`);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error("获取酒店信息失败:", error);
      throw error;
    }
  },

  // 获取酒店房型
  getHotelRoomTypes: async (innId, languageCode = 0) => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/hotels/${innId}/room-types`,
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error("获取酒店房型失败:", error);
      throw error;
    }
  },

  // 获取酒店实时房态（从本地数据库获取预付价格）
  getHotelRoomStatus: async (innId, endOfDay, days = 1, priceType = 1) => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/hotels/${innId}/room-status`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            endOfDay,
            days,
            priceType,
          }),
        },
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error("获取酒店实时房态失败:", error);
      throw error;
    }
  },

  // 获取酒店图片
  getHotelImages: async (innId, imageType = null, languageCode = 0) => {
    try {
      const queryParams = new URLSearchParams();
      if (imageType) queryParams.append("imageType", imageType);
      if (languageCode !== null)
        queryParams.append("languageCode", languageCode);

      const url = `/images/${innId}${queryParams.toString() ? "?" + queryParams.toString() : ""}`;
      const response = await fetch(`${API_BASE_URL}${url}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error("获取酒店图片失败:", error);
      throw error;
    }
  },
};

// 品牌相关API
export const brandApi = {
  // 获取品牌列表
  getBrandList: async (params = {}) => {
    const defaultParams = {
      languageCode: 0,
      ...params,
    };
    return apiCall("/brand/getBrandList", "POST", defaultParams);
  },
};

// 更新订单相关API，合并所有方法
export const orderApi = {
  // 获取订单列表
  getOrderList: async (params = {}) => {
    try {
      const queryParams = new URLSearchParams();
      if (params.customerId)
        queryParams.append("customerId", params.customerId);
      if (params.status) queryParams.append("status", params.status);
      if (params.pageNum) queryParams.append("pageNum", params.pageNum);
      if (params.pageSize) queryParams.append("pageSize", params.pageSize);

      const BASE_URL =
        process.env.NODE_ENV === "production"
          ? "/api"
          : "http://localhost:8082/api";
      const url = `/orders${queryParams.toString() ? "?" + queryParams.toString() : ""}`;
      console.log("调用订单API:", `${BASE_URL}${url}`);

      const response = await fetch(`${BASE_URL}${url}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      console.log("API响应状态:", response.status);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      console.log("API响应数据:", result);
      return result;
    } catch (error) {
      console.error("获取订单列表失败:", error);
      throw error;
    }
  },

  // 获取订单详情
  getOrderDetail: async (orderId) => {
    try {
      const BASE_URL =
        process.env.NODE_ENV === "production"
          ? "/api"
          : "http://localhost:8082/api";
      const response = await fetch(`${BASE_URL}/orders/${orderId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error("获取订单详情失败:", error);
      throw error;
    }
  },

  // 创建订单
  createOrder: async (orderData) => {
    try {
      const BASE_URL =
        process.env.NODE_ENV === "production"
          ? "/api"
          : "http://localhost:8082/api";
      const response = await fetch(`${BASE_URL}/orders`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(orderData),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error("创建订单失败:", error);
      throw error;
    }
  },

  // 下单预订（别名方法）
  postOrder: async (orderData) => {
    return orderApi.createOrder(orderData);
  },

  // 取消订单
  cancelOrder: async (orderId) => {
    try {
      const BASE_URL =
        process.env.NODE_ENV === "production"
          ? "/api"
          : "http://localhost:8082/api";
      const response = await fetch(`${BASE_URL}/orders/${orderId}/cancel`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      return result;
    } catch (error) {
      console.error("取消订单失败:", error);
      throw error;
    }
  },
};

// 错误处理工具函数
export const handleApiError = (error, fallbackData = []) => {
  console.error("API错误:", error);
  return {
    success: false,
    error: error.message,
    data: fallbackData,
  };
};

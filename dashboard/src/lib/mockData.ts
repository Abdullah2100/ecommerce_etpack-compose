export const mockProducts = [
    {
        id: "1",
        thmbnail: "/images/product.svg",
        name: "Premium Wireless Headphones",
        price: 299.99,
        store: "Tech Haven",
        subcategory: "Audio",
        productVarients: [
            [{ varientName: "Color", name: "Black", percentage: "100%" }],
            [{ varientName: "Color", name: "White", percentage: "50%" }]
        ]
    },
    {
        id: "2",
        thmbnail: "/images/product.svg",
        name: "Ergonomic Office Chair",
        price: 450.00,
        store: "Office Depot",
        subcategory: "Furniture",
        productVarients: [
            [{ varientName: "Material", name: "Leather", percentage: "100%" }],
            [{ varientName: "Material", name: "Mesh", percentage: "80%" }]
        ]
    },
    {
        id: "3",
        thmbnail: "/images/product.svg",
        name: "Smart Watch Series 5",
        price: 399.00,
        store: "Gadget World",
        subcategory: "Wearables",
        productVarients: [
            [{ varientName: "Band", name: "Silicon", percentage: "100%" }]
        ]
    },
    {
        id: "4",
        thmbnail: "/images/product.svg",
        name: "Mechanical Keyboard",
        price: 120.50,
        store: "Keychron",
        subcategory: "Accessories",
        productVarients: []
    },
    {
        id: "5",
        thmbnail: "/images/product.svg",
        name: "Gaming Mouse",
        price: 59.99,
        store: "Razer Store",
        subcategory: "Accessories",
        productVarients: []
    }
];

export const mockOrders = [
    {
        id: "101",
        name: "John Doe",
        user_phone: "+1 234 567 890",
        status: 0, // Pending
        totalPrice: 150.00
    },
    {
        id: "102",
        name: "Jane Smith",
        user_phone: "+1 987 654 321",
        status: 1, // Processing
        totalPrice: 299.99
    },
    {
        id: "103",
        name: "Alice Johnson",
        user_phone: "+1 555 123 456",
        status: 2, // Shipped
        totalPrice: 45.50
    },
    {
        id: "104",
        name: "Bob Brown",
        user_phone: "+1 444 777 888",
        status: 3, // Delivered
        totalPrice: 1200.00
    },
    {
        id: "105",
        name: "Charlie Davis",
        user_phone: "+1 222 333 444",
        status: 4, // Cancelled
        totalPrice: 15.00
    }
];

export const mockUsers = [
    {
        id: "u1",
        thumbnail: "/images/user.svg",
        name: "Admin User",
        phone: "+1 000 000 000",
        email: "admin@example.com",
        store_id: "s1",
        storeName: "Main Store",
        isAdmin: true,
        isActive: true
    },
    {
        id: "u2",
        thumbnail: "/images/user.svg",
        name: "Regular Customer",
        phone: "+1 111 222 333",
        email: "customer@example.com",
        store_id: null,
        storeName: "-",
        isAdmin: false,
        isActive: true
    },
    {
        id: "u3",
        thumbnail: "/images/user.svg",
        name: "Blocked User",
        phone: "+1 999 888 777",
        email: "blocked@example.com",
        store_id: null,
        storeName: "-",
        isAdmin: false,
        isActive: false
    }
];

export const mockOrderStatus = ["Pending", "Processing", "Shipped", "Delivered", "Cancelled"];

export const mockStores = [
    {
        id: "s1",
        small_image: "/images/store.svg",
        name: "Tech Haven",
        userName: "John Doe",
        created_at: "2023-01-15T10:00:00",
        isBlocked: false
    },
    {
        id: "s2",
        small_image: "/images/store.svg",
        name: "Fashion Hub",
        userName: "Jane Smith",
        created_at: "2023-02-20T14:30:00",
        isBlocked: false
    },
    {
        id: "s3",
        small_image: "/images/store.svg",
        name: "Gadget World",
        userName: "Alice Johnson",
        created_at: "2023-03-10T09:15:00",
        isBlocked: true
    }
];

export const mockCategories = [
    {
        id: "c1",
        name: "Electronics",
        image: "/images/category.svg"
    },
    {
        id: "c2",
        name: "Clothing",
        image: "/images/category.svg"
    },
    {
        id: "c3",
        name: "Home & Garden",
        image: "/images/category.svg"
    },
    {
        id: "c4",
        name: "Toys",
        image: "/images/category.svg"
    }
];

export const mockMyInfo = {
    name: "Abdullah Al-Ghamdi",
    phone: "+966 50 123 4567",
    email: "abdullah@example.com",
    thumbnail: "/images/user.svg"
};

export const mockAnalytics = {
    totalRevenue: 15420,
    totalOrders: 1250,
    totalUsers: 3200,
    totalProducts: 450,
    revenueData: [
        { name: 'Jan', value: 4000 },
        { name: 'Feb', value: 3000 },
        { name: 'Mar', value: 2000 },
        { name: 'Apr', value: 2780 },
        { name: 'May', value: 1890 },
        { name: 'Jun', value: 2390 },
        { name: 'Jul', value: 3490 },
    ],
    ordersData: [
        { name: 'Jan', value: 2400 },
        { name: 'Feb', value: 1398 },
        { name: 'Mar', value: 9800 },
        { name: 'Apr', value: 3908 },
        { name: 'May', value: 4800 },
        { name: 'Jun', value: 3800 },
        { name: 'Jul', value: 4300 },
    ]
};

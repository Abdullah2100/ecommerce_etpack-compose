import React from 'react';
import { DollarSign, Users, ShoppingBag, Package, MapPin } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card/card';
import { mockAnalytics } from '@/lib/mockData';
import { useQuery } from '@tanstack/react-query';
import { getAnalyse } from '@/lib/api/analyse';

const Analytics = () => {
    const { totalRevenue, totalOrders, totalUsers, totalProducts, revenueData } = mockAnalytics;

    const stats = [
        {
            title: "Total Revenue",
            value: `$${totalRevenue.toLocaleString()}`,
            change: "+20.1% from last month",
            icon: DollarSign,
            color: "text-green-500",
            bg: "bg-green-500/10"
        },
        {
            title: "Total Orders",
            value: totalOrders.toLocaleString(),
            change: "+180.1% from last month",
            icon: ShoppingBag,
            color: "text-blue-500",
            bg: "bg-blue-500/10"
        },
        {
            title: "Total Users",
            value: totalUsers.toLocaleString(),
            change: "+19% from last month",
            icon: Users,
            color: "text-orange-500",
            bg: "bg-orange-500/10"
        },
        {
            title: "Total Delivery Distance",
            value: totalProducts.toLocaleString(),
            change: "+201 since last hour",
            icon: MapPin,
            color: "text-purple-500",
            bg: "bg-purple-500/10"
        },
        {
            title: "Total Product",
            value: totalProducts.toLocaleString(),
            change: "+201 since last hour",
            icon: Package,
            color: "text-purple-500",
            bg: "bg-purple-500/10"
        },
    ];

    const { data, isLoading, isError } = useQuery({

        queryKey: ["analytics"],
        queryFn: () => getAnalyse(),

    });

    if (isLoading || isError) return null;

    console.log("data is ", JSON.stringify(data))

    const anylyseByIndex = (index: number) => {
        switch (index) {
            case 0: return data?.totalFee
            case 1: return data?.totalOrders
            case 2: return data?.usersCount
            case 3: return data?.totalDeliveryDistance
            case 4: return data?.productCount
        }
    }


    return (
        <div className="flex flex-col w-full h-full space-y-6 p-6 animate-in fade-in duration-500">
            <h1 className="text-3xl font-bold tracking-tight bg-gradient-to-r from-primary to-indigo-600 bg-clip-text text-transparent">
                Analytics Dashboard
            </h1>

            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
                {stats.map((stat, index) => (
                    <Card key={index} className="border-border/50 bg-card/50 backdrop-blur-sm hover:shadow-lg transition-all duration-300">
                        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                            <CardTitle className="text-sm font-medium text-muted-foreground">
                                {stat.title}
                            </CardTitle>
                            <div className={`p-2 rounded-full ${stat.bg}`}>
                                <stat.icon className={`h-4 w-4 ${stat.color}`} />
                            </div>
                        </CardHeader>
                        <CardContent>
                            <div className="text-2xl font-bold">{anylyseByIndex(index)}</div>
                        </CardContent>
                    </Card>
                ))}
            </div>

            {/* <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
                <Card className="col-span-4 border-border/50 bg-card/50 backdrop-blur-sm">
                    <CardHeader>
                        <CardTitle>Revenue Overview</CardTitle>
                    </CardHeader>
                    <CardContent className="pl-2">
                        <div className="h-[350px] w-full flex items-end justify-between gap-2 px-4 pt-4">
                            {revenueData.map((data, i) => (
                                <div key={i} className="flex flex-col items-center gap-2 w-full group">
                                    <div className="relative w-full flex justify-center items-end h-[300px]">
                                        <div
                                            className="w-full max-w-[40px] bg-primary/80 hover:bg-primary rounded-t-md transition-all duration-500 group-hover:shadow-[0_0_15px_rgba(var(--primary),0.5)]"
                                            style={{ height: `${(data.value / maxRevenue) * 100}%` }}
                                        >
                                            <div className="opacity-0 group-hover:opacity-100 absolute -top-8 left-1/2 -translate-x-1/2 bg-popover text-popover-foreground text-xs py-1 px-2 rounded shadow-sm transition-opacity whitespace-nowrap z-10">
                                                ${data.value}
                                            </div>
                                        </div>
                                    </div>
                                    <span className="text-xs text-muted-foreground font-medium">{data.name}</span>
                                </div>
                            ))}
                        </div>
                    </CardContent>
                </Card>

                <Card className="col-span-3 border-border/50 bg-card/50 backdrop-blur-sm">
                    <CardHeader>
                        <CardTitle>Recent Activity</CardTitle>
                        <div className="text-sm text-muted-foreground">
                            Latest transactions from your store.
                        </div>
                    </CardHeader>
                    <CardContent>
                        <div className="space-y-8">
                            {[1, 2, 3, 4, 5].map((_, index) => (
                                <div key={index} className="flex items-center group cursor-pointer hover:bg-muted/50 p-2 rounded-lg transition-colors">
                                    <div className="h-9 w-9 rounded-full bg-primary/10 flex items-center justify-center text-sm font-medium text-primary group-hover:bg-primary group-hover:text-primary-foreground transition-colors">
                                        {["OM", "JL", "IN", "WK", "SD"][index]}
                                    </div>
                                    <div className="ml-4 space-y-1">
                                        <p className="text-sm font-medium leading-none">User {index + 1}</p>
                                        <p className="text-sm text-muted-foreground">
                                            user{index + 1}@example.com
                                        </p>
                                    </div>
                                    <div className="ml-auto font-medium text-green-600">+$1,999.00</div>
                                </div>
                            ))}
                        </div>
                    </CardContent>
                </Card>
            </div> */}
        </div>
    );
};

export default Analytics;

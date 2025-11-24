import { mockOrders, mockOrderStatus } from "@/lib/mockData";
import { useState } from "react";
import { ToastContainer } from "react-toastify";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

const Order = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const orders = mockOrders;
  const pageNumb = 1;
  const orderStatus = mockOrderStatus;

  return (
    <div className="flex flex-col w-full h-full space-y-6 p-6 animate-in fade-in duration-500">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold tracking-tight bg-gradient-to-r from-primary to-blue-600 bg-clip-text text-transparent">
          Orders
        </h1>
      </div>

      <div className="w-full overflow-hidden rounded-xl border border-border/50 bg-card/50 backdrop-blur-sm shadow-sm">
        <div className="overflow-x-auto">
          <table className="w-full text-sm text-left">
            <thead className="bg-muted/30 text-muted-foreground uppercase text-xs font-semibold tracking-wider">
              <tr>
                <th className="px-6 py-4">#</th>
                <th className="px-6 py-4">Customer</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4 text-right">Total</th>
                <th className="px-6 py-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border/50">
              {orders.map((value, index) => (
                <tr key={index} className="group hover:bg-muted/30 transition-all duration-200">
                  <td className="px-6 py-4 text-muted-foreground font-mono text-xs">#{value.id}</td>
                  <td className="px-6 py-4">
                    <div className="flex flex-col">
                      <span className="font-medium text-foreground group-hover:text-primary transition-colors">{value.name}</span>
                      <span className="text-xs text-muted-foreground">{value.user_phone}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <DropdownMenu>
                      <DropdownMenuTrigger asChild>
                        <Button
                          variant="outline"
                          size="sm"
                          className={`
                                w-[140px] justify-between border-transparent bg-opacity-10 hover:bg-opacity-20 transition-colors
                                ${value.status === 0 ? 'bg-yellow-500 text-yellow-600 hover:text-yellow-700' : ''}
                                ${value.status === 1 ? 'bg-blue-500 text-blue-600 hover:text-blue-700' : ''}
                                ${value.status === 2 ? 'bg-purple-500 text-purple-600 hover:text-purple-700' : ''}
                                ${value.status === 3 ? 'bg-green-500 text-green-600 hover:text-green-700' : ''}
                                ${value.status === 4 ? 'bg-red-500 text-red-600 hover:text-red-700' : ''}
                            `}
                        >
                          {orderStatus[value.status]}
                        </Button>
                      </DropdownMenuTrigger>
                      <DropdownMenuContent align="start" className="w-[140px]">
                        {orderStatus.map((statusItem, sIndex) => (
                          <DropdownMenuItem
                            key={sIndex}
                            onClick={() => {
                              // Mock update logic would go here
                              console.log(`Update order ${value.id} to ${statusItem}`);
                            }}
                          >
                            {statusItem}
                          </DropdownMenuItem>
                        ))}
                      </DropdownMenuContent>
                    </DropdownMenu>
                  </td>
                  <td className="px-6 py-4 font-medium text-right">
                    ${value.totalPrice.toFixed(2)}
                  </td>
                  <td className="px-6 py-4 text-right">
                    <button className="text-muted-foreground hover:text-primary transition-colors">
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="lucide lucide-eye"><path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z" /><circle cx="12" cy="12" r="3" /></svg>
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="flex justify-center mt-6">
        <div className="flex gap-2">
          {Array.from({ length: pageNumb }, (_, i) => (
            <button
              key={i}
              onClick={() => setCurrentPage(i + 1)}
              className={`
                  h-9 w-9 flex items-center justify-center rounded-lg text-sm font-medium transition-all duration-200
                  ${currentPage === i + 1
                  ? 'bg-primary text-primary-foreground shadow-md scale-105'
                  : 'bg-card border border-border hover:bg-accent hover:text-accent-foreground'
                }
              `}
            >
              {i + 1}
            </button>
          ))}
        </div>
      </div>
      <ToastContainer />
    </div>
  );
};
export default Order;

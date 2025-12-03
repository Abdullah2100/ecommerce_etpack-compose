using api.application;
using api.domain.entity;
using api.domain.Interface;
using Microsoft.EntityFrameworkCore;

namespace api.Infrastructure.Repositories;

public class CurrencyRepository(AppDbContext context):ICurrencyRepository
{
    public void Add(Currency entity)
    {
        context.Add(entity);
    }

    public void Update(Currency entity)
    {
        context.Update(entity);
    }
    
    public async Task<Currency?> GetCurrency(Guid id )
    {
        Currency? element = await context.Payments.FirstOrDefaultAsync(x=>x.Id == id);
    return element;
    }

    public async Task<List<Currency>> GetAll(int pageNum,int pageSize)
    {
        return await context.Payments
            .Take(pageSize)
            .Skip((pageNum-1)*pageSize)
            .ToListAsync();
    }

    public async Task Delete(Guid id)
    {
        Currency? element = await context.Payments.FirstOrDefaultAsync(x => x.Id == id);

        if (element is null) return;
        context.Payments.Remove(element);
    }
}
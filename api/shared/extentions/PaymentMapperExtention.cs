using api.application.Interface.payment;
using api.domain.entity;
using api.Presentation.dto;

namespace api.shared.extentions;

public static class PaymentMapperExtention
{
    public static CurrencyDto toPaymentDto(this Currency currency)
    {
        return new CurrencyDto
        {
            Id = currency.Id,
            CreatedAt = currency.CreatedAt,
            UpdatedAt = currency.UpdatedAt,
            Name = currency.Name,
            value= currency.Value,
            image = currency.Symbol
        };
    }
}
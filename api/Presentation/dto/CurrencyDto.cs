using System.ComponentModel.DataAnnotations;

namespace api.Presentation.dto;

public class CurrencyDto
{
    public Guid Id { get; set; } 
    public string Name { get; set; }
    public string image { get; set; }
    public DateTime CreatedAt { get; set; }
    public DateTime? UpdatedAt { get; set; } = null;
    public int value { get; set; }
}


public class CreateCurrencyDto
{
    [MaxLength(20)]

    public string Name { get; set; }
    [MaxLength()]
    public String Symbol { get; set; } 
    public int Value { get; set; } 
}

public class UpdateCurrencyDto
{
    public Guid Id { get; set; }
    [MaxLength(20)]
    public string? Name { get; set; } = null;
    
    [MaxLength()]
    public String? Symbol { get; set; } = null;
    public int? Value { get; set; } = null;
}
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace api.domain.entity;

public class Currency
{
    [Key]
    public Guid Id { get; set; } 
    
    [Column(TypeName = "nvarchar(20)")]
    public string Name { get; set; }
    public int Value { get; set; }
    
    [Column(TypeName = "nvarchar(5)")]
    public string Symbol { get; set; }
    
    [Column(TypeName = "Timestamp")]
    public DateTime CreatedAt { get; set; }

    [Column(TypeName = "Timestamp")] 
    public DateTime? UpdatedAt { get; set; } = null; 
}
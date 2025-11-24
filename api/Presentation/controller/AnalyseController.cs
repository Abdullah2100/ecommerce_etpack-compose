using System.Security.Claims;
using api.application.Interface;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Primitives;

namespace api.Presentation.controller;

[Authorize]
[ApiController]
[Route("api/analyse")]
public class AnalyseController(
    IAnalyseServices analyseServices,
    IAuthenticationService authenticationService) : ControllerBase
{
    [HttpGet("currentMonth")]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    [ProducesResponseType(StatusCodes.Status401Unauthorized)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    public async Task<IActionResult> GetOrderStatus()
    {
        StringValues authorizationHeader = HttpContext.Request.Headers["Authorization"];
        Claim? id = authenticationService.GetPayloadFromToken("id",
            authorizationHeader.ToString().Replace("Bearer ", ""));
        Guid? adminId = null;
        if (Guid.TryParse(id?.Value.ToString(), out Guid outId))
        {
            adminId = outId;
        }

        if (adminId is null)
        {
            return Unauthorized("هناك مشكلة في التحقق");
        }

        var result = await analyseServices.GetMonthAnalysis(adminId.Value);

        return result.IsSuccessful switch
        {
            true => StatusCode(result.StatusCode, result.Data),
            _ => StatusCode(result.StatusCode, result.Message)
        };
    }
}
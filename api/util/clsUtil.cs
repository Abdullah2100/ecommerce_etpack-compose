using System.Security.Cryptography;
using System.Text;
using api.application;

namespace api.util
{
    public enum EnImageType
    {
        Profile,
        Product,
        Category,
        Store,
        Banner,
        Delivery,
        Payment,
    };

    static class ClsUtil
    {
        public static Guid GenerateGuid() => Guid.NewGuid();

        public static DateTime GenerateDateTime(EnTokenMode mode)
        {
            switch (mode)
            {
                case EnTokenMode.AccessToken:
                {
                    return DateTime.Now.AddSeconds(40);
                }
                default:
                {
                    return DateTime.Now.AddDays(30);
                }
            }
        }
        public static string RemoveAdditionalPath(string dir)
        {
            if (dir.Contains("http://72.60.232.89:5077/staticFiles"))
            {
                return dir.Replace("http://72.60.232.89:5077/staticFiles", "");
            }else if (dir.Contains("http://localhost:5077/staticFiles"))
            {
                return dir.Replace("http://localhost:5077/staticFiles", "");

            }
            else
                return dir;
        }


        public static string HashingText(string? text)
        {
            if (text is null) return "";
            
            using SHA256 sha256 = SHA256.Create();
            // Compute the hash of the given string
            byte[] hashValue = sha256.ComputeHash(Encoding.UTF8.GetBytes(text));

            // Convert the byte array to string format
            return BitConverter.ToString(hashValue).Replace("-", "");
        }
    }
}
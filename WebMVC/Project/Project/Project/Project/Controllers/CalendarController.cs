using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Dapper;
using Project.Models;
using System.Configuration;


namespace Project.Controllers
{
    /// <summary>
    /// 網頁事件記錄圖表
    /// </summary>
    public class Calendar
    {
        
        public List<CalendarViewModel> GetAll(string UserName)
        {
            using (var sqlConnection = new SqlConnection(ConfigurationManager.ConnectionStrings["DefaultConnection"].ToString()))
            {
                string queryStr = @"Select id   from P1_User_Account where email = @input  ";
                var datas = sqlConnection.Query<int>(queryStr, new { input = UserName }).ToArray();
                 queryStr = @"Select   * from P1_record where  user_id=@louis   ";
                var datas2 = sqlConnection.Query<CalendarViewModel>(queryStr, new { louis = datas[0] }).ToList();
                return datas2;
            }
        }
    }
}

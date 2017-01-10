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
    /// 網頁記帳圖表
    /// </summary>
    public class Money
    {
        
        public List<MoneyViewModel> GetAll()
        {
            using (var sqlConnection = new SqlConnection(ConfigurationManager.ConnectionStrings["DefaultConnection"].ToString()))
            {
                string queryStr = @"Select * from P1_Overspend";
                var datas = sqlConnection.Query<MoneyViewModel>(queryStr).ToList();
                return datas;
            }
        }
    }
}

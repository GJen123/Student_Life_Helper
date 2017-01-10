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
    /// 群組狀態圖表
    /// </summary>
    public class Event
    {
        
        public List<EventViewModel> GetAll()
        {
            using (var sqlConnection = new SqlConnection(ConfigurationManager.ConnectionStrings["DefaultConnection"].ToString()))
            {
                string queryStr = @"Select * from P1_Group";
                var datas = sqlConnection.Query<EventViewModel>(queryStr).ToList();
                 
                return datas;
            }
        }
    }
}

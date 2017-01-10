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
    /// 預算科目服務
    /// </summary>
    public class RecordCorrect
    {
        
        public List<RecordViewModel> GetAll()
        {
            using (var sqlConnection = new SqlConnection(ConfigurationManager.ConnectionStrings["DefaultConnection"].ToString()))
            {
                string queryStr = @"Select * from P1_Record_Correct2";
                var datas = sqlConnection.Query<RecordViewModel>(queryStr).ToList();
                return datas;
            }
        }
    }
}

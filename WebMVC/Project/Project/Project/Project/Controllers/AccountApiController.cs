using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.Security.Cryptography;
using System.Web.Security;
using System.Text;
using System.Data.Entity.Validation;
using System.Web.Http.Description;
using Project;

namespace Project.Controllers
{
    [System.Web.Mvc.HandleError]
    public class AccountApiController : ApiController
    {
        ProjectEntities db = new ProjectEntities();


        //api/AccountApi/Register
        [HttpPost]
        public HttpResponseMessage Register(P1_User_Account p1_user_account)
        {
            if (p1_user_account == null)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }
            if (ModelState.IsValid)
            {
                /*MD5 md5 = MD5.Create();
                byte[] source = Encoding.Default.GetBytes(p2_user_account.email + p2_user_account.password);
                byte[] crypto = md5.ComputeHash(source);//進行MD5加密
                string result = Convert.ToBase64String(crypto);//把加密後的字串從Byte[]轉為字串

                p2_user_account.password = result;*/

                db.P1_User_Account.Add(p1_user_account);
                db.SaveChanges();

                HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.Created, p1_user_account.id);
                response.Headers.Location = new Uri(Url.Link("DefaultApi", new { id = p1_user_account.id }));
                return response;
            }
            else
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ModelState);
            }
        }
        

        [HttpPost]
        public HttpResponseMessage Login(P1_User_Account p1_user_account)
        {


            if (p1_user_account == null)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }

            if (ModelState.IsValid)
            {

                var p = (from d in db.P1_User_Account where d.email.Equals(p1_user_account.email) select d).FirstOrDefault();
                var q = (from b in db.P1_User_Account where b.password.Equals(p1_user_account.password) select b).FirstOrDefault();

                if (p == null)
                {
                    var result = "email is false";
                    return Request.CreateResponse(HttpStatusCode.NotFound,result);
                }

                else if (q == null)
                {
                    var result = "password is false";
                    return Request.CreateResponse(HttpStatusCode.NotFound,result);
                }

                var a = (from c in db.P1_User_Account where c.email.Equals(p1_user_account.email) select c.id).FirstOrDefault();


                HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.OK, a);
                response.Headers.Location = new Uri(Url.Link("DefaultApi", new { id = p1_user_account.id }));
                return response;
            }
            else
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ModelState);
            }

        }

        protected override void Dispose(bool disposing)
        {
            db.Dispose();
            base.Dispose(disposing);
        }
    }
}

using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using Project;

namespace Project.Controllers
{
    public class PredictApiController : ApiController
    {
        private ProjectEntities db = new ProjectEntities();
        
        // GET: api/TestApi
        public IQueryable<P1_Record> GetP2_Record()
        {
            return db.P1_Record;
        }

        // GET: api/TestApi/5
        [ResponseType(typeof(P1_Record))]
        public IHttpActionResult GetP2_Record(int id)
        {
            P1_Record p1_Record = db.P1_Record.Find(id);
            if (p1_Record == null)
            {
                return NotFound();
            }

            return Ok(p1_Record);
        }

        //過去紀錄計算次數
        [HttpGet]
        [ResponseType(typeof(P1_Record))]
        public IHttpActionResult Getcount(int id)
        {
            var q = from p in db.P1_Record
                    where p.weekly.Equals(id) & p.schedule == false
                    group p by p.@event into g
                    select new
                    {
                        g.Key,
                        num = g.Count()
                    };


            return Ok(q);

        }
        
        //記錄次數取最大值
        [HttpGet]
        [ResponseType(typeof(P1_Record))]
        public IHttpActionResult GetMax(int id)
        {


            var query = from p in db.P1_Record
                        where p.weekly.Equals(id) & p.schedule == false
                        group p by p.@event into g
                        select new
                        {
                            Name = g.Key,
                            Num = g.Count(),
                            member = g,
                        };
            var max = query.Max(x => x.Num);
            var temp = from a in query
                       where a.Num.Equals(max)
                       select a.member.FirstOrDefault();
            

            return Ok(temp);
          
        }

        //最大值相同取最新記錄
        [HttpGet]
        public IHttpActionResult GetNew()
        {
            var q = db.P1_Record.Select(x => x.time).Max(); 
            return Ok(q);
        }

        // PUT: api/TestApi/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutP2_Record(int id, P1_Record p1_Record)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != p1_Record.r_id)
            {
                return BadRequest();
            }

            db.Entry(p1_Record).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!P1_RecordExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/TestApi
        [ResponseType(typeof(P1_Record))]
        public IHttpActionResult PostP2_Record(P1_Record p1_Record)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.P1_Record.Add(p1_Record);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = p1_Record.r_id }, p1_Record);
        }

        // DELETE: api/TestApi/5
        [ResponseType(typeof(P1_Record))]
        public IHttpActionResult DeleteP2_Record(int id)
        {
            P1_Record p1_Record = db.P1_Record.Find(id);
            if (p1_Record == null)
            {
                return NotFound();
            }

            db.P1_Record.Remove(p1_Record);
            db.SaveChanges();

            return Ok(p1_Record);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool P1_RecordExists(int id)
        {
            return db.P1_Record.Count(e => e.r_id == id) > 0;
        }
    }
    internal class Student
    {
        public string id { get; set; }
        public string location { get; set; }
        public string name { get; set; }
        public string phone { get; set; }
        public int score { get; set; }
    }
}
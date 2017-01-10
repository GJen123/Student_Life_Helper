using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using System.Configuration;
using Project;

namespace Project.Controllers
{
    public class P2_RecordApiController : ApiController
    {
        private ProjectEntities db = new ProjectEntities();

        // GET: api/P2_Record
        public IQueryable<P1_Record> GetP2_Record()
        {
            return db.P1_Record;
        }

        // GET: api/P2_Record/5
        //search by id
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

        

        //api/P2_Record/GetP2_RecordByWeekly/5
        //search by weekly
        //依照星期抓值
        public IEnumerable<P1_Record> GetP2_RecordByWeekly(int id)
        {
            var p = (from d in db.P1_Record where d.weekly.Equals(id) & d.schedule == true  select d).AsEnumerable();
            return p;
        }

        // PUT: api/P2_Record/5
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

        // POST: api/P2_Record
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

        // DELETE: api/P2_Record/5
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
}
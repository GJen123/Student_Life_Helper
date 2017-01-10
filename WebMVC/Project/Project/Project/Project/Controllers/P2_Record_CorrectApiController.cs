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
    public class P2_Record_CorrectApiController : ApiController
    {
        private ProjectEntities db = new ProjectEntities();

        // GET: api/P1_Record_Correct
        public IQueryable<P1_Record_Correct> GetP1_Record_Correct()
        {
            return db.P1_Record_Correct;
        }

        // GET: api/P1_Record_Correct/5
        [ResponseType(typeof(P1_Record_Correct))]
        public IHttpActionResult GetP1_Record_Correct(int id)
        {
            P1_Record_Correct p1_Record_Correct = db.P1_Record_Correct.Find(id);
            if (p1_Record_Correct == null)
            {
                return NotFound();
            }

            return Ok(p1_Record_Correct);
        }

        // PUT: api/P1_Record_Correct/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutP1_Record_Correct(int id, P1_Record_Correct p1_Record_Correct)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != p1_Record_Correct.rc_id)
            {
                return BadRequest();
            }

            db.Entry(p1_Record_Correct).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!P1_Record_CorrectExists(id))
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

        // POST: api/P1_Record_Correct
        [ResponseType(typeof(P1_Record_Correct))]
        public IHttpActionResult PostP1_Record_Correct(P1_Record_Correct p1_Record_Correct)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.P1_Record_Correct.Add(p1_Record_Correct);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = p1_Record_Correct.rc_id }, p1_Record_Correct);
        }

        // DELETE: api/P1_Record_Correct/5
        [ResponseType(typeof(P1_Record_Correct))]
        public IHttpActionResult DeleteP1_Record_Correct(int id)
        {
            P1_Record_Correct p1_Record_Correct = db.P1_Record_Correct.Find(id);
            if (p1_Record_Correct == null)
            {
                return NotFound();
            }

            db.P1_Record_Correct.Remove(p1_Record_Correct);
            db.SaveChanges();

            return Ok(p1_Record_Correct);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool P1_Record_CorrectExists(int id)
        {
            return db.P1_Record_Correct.Count(e => e.rc_id == id) > 0;
        }
    }
}
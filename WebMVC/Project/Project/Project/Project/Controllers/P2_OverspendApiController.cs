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
    public class P2_OverspendApiController : ApiController
    {
        private ProjectEntities db = new ProjectEntities();

        // GET: api/P2_OverspendApi
        public IQueryable<P1_Overspend> GetP1_Overspend()
        {
            return db.P1_Overspend;
        }

        // GET: api/P2_OverspendApi/5
        [ResponseType(typeof(P1_Overspend))]
        public IHttpActionResult GetP1_Overspend(int id)
        {
            P1_Overspend p1_Overspend = db.P1_Overspend.Find(id);
            if (p1_Overspend == null)
            {
                return NotFound();
            }

            return Ok(p1_Overspend);
        }

        // PUT: api/P2_OverspendApi/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutP1_Overspend(int id, P1_Overspend p1_Overspend)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != p1_Overspend.O_id)
            {
                return BadRequest();
            }

            db.Entry(p1_Overspend).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!P1_OverspendExists(id))
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

        // POST: api/P2_OverspendApi
        [ResponseType(typeof(P1_Overspend))]
        public IHttpActionResult PostP1_Overspend(P1_Overspend p1_Overspend)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.P1_Overspend.Add(p1_Overspend);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = p1_Overspend.O_id }, p1_Overspend);
        }

        // DELETE: api/P2_OverspendApi/5
        [ResponseType(typeof(P1_Overspend))]
        public IHttpActionResult DeleteP1_Overspend(int id)
        {
            P1_Overspend p1_Overspend = db.P1_Overspend.Find(id);
            if (p1_Overspend == null)
            {
                return NotFound();
            }

            db.P1_Overspend.Remove(p1_Overspend);
            db.SaveChanges();

            return Ok(p1_Overspend);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool P1_OverspendExists(int id)
        {
            return db.P1_Overspend.Count(e => e.O_id == id) > 0;
        }
    }
}
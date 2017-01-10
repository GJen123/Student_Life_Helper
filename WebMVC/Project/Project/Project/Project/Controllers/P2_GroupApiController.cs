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
    public class P2_GroupApiController : ApiController
    {
        private ProjectEntities db = new ProjectEntities();

        // GET: api/P2_GroupApi
        public IQueryable<P1_Group> GetP1_Group()
        {
            return db.P1_Group;
        }

        // GET: api/P2_GroupApi/5
        [ResponseType(typeof(P1_Group))]
        public IHttpActionResult GetP1_Group(int id)
        {
            P1_Group p1_Group = db.P1_Group.Find(id);
            if (p1_Group == null)
            {
                return NotFound();
            }

            return Ok(p1_Group);
        }

        // PUT: api/P2_GroupApi/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutP1_Group(int id, P1_Group p1_Group)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != p1_Group.U_id)
            {
                return BadRequest();
            }

            db.Entry(p1_Group).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!P1_GroupExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.OK);
        }

        // POST: api/P2_GroupApi
        [ResponseType(typeof(P1_Group))]
        public IHttpActionResult PostP1_Group(P1_Group p1_Group)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.P1_Group.Add(p1_Group);
            db.SaveChanges();

            return CreatedAtRoute("DefaultApi", new { id = p1_Group.G_id }, p1_Group);
        }

        // DELETE: api/P2_GroupApi/5
        [ResponseType(typeof(P1_Group))]
        public IHttpActionResult DeleteP1_Group(int id)
        {
            P1_Group p1_Group = db.P1_Group.Find(id);
            if (p1_Group == null)
            {
                return NotFound();
            }

            db.P1_Group.Remove(p1_Group);
            db.SaveChanges();

            return Ok(p1_Group);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool P1_GroupExists(int id)
        {
            return db.P1_Group.Count(e => e.G_id == id) > 0;
        }
    }
}
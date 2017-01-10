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
using System.Data.Entity.Validation;

namespace Project.Controllers
{
    public class P2_User_DetailApiController : ApiController
    {
        private ProjectEntities db = new ProjectEntities();

        // GET: api/P2_User_DetailApi
        public IQueryable<P1_User_Detail> GetP2_User_Detail()
        {
            return db.P1_User_Detail;
        }

        // GET: api/P2_User_DetailApi/5
        [ResponseType(typeof(P1_User_Detail))]
        public IHttpActionResult GetP2_User_Detail(int id)
        {
            P1_User_Detail p1_User_Detail = db.P1_User_Detail.Find(id);
            if (p1_User_Detail == null)
            {
                return NotFound();
            }

            return Ok(p1_User_Detail);
        }

        // PUT: api/P2_User_DetailApi/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutP2_User_Detail(int id, P1_User_Detail p1_User_Detail)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != p1_User_Detail.id)
            {
                return BadRequest();
            }

            db.Entry(p1_User_Detail).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!P1_User_DetailExists(id))
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

        // POST: api/P2_User_DetailApi/PostP2_User_Detail
        [ResponseType(typeof(P1_User_Detail))]
        public IHttpActionResult PostP2_User_Detail(P1_User_Detail p1_User_Detail)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            

            try
            {
                db.P1_User_Detail.Add(p1_User_Detail);
                db.SaveChanges();
                
            }
            catch (DbEntityValidationException ex)
            {
                var entityError = ex.EntityValidationErrors.SelectMany(x => x.ValidationErrors).Select(x => x.ErrorMessage);
                var getFullMessage = string.Join("; ", entityError);
                var exceptionMessage = string.Concat(ex.Message, "error are: ", getFullMessage);

                throw new DbEntityValidationException(exceptionMessage, ex.EntityValidationErrors);
            }

            return CreatedAtRoute("DefaultApi", new { id = p1_User_Detail.id }, p1_User_Detail);
        }

        // DELETE: api/P2_User_DetailApi/5
        [ResponseType(typeof(P1_User_Detail))]
        public IHttpActionResult DeleteP2_User_Detail(int id)
        {
            P1_User_Detail p1_User_Detail = db.P1_User_Detail.Find(id);
            if (p1_User_Detail == null)
            {
                return NotFound();
            }

            db.P1_User_Detail.Remove(p1_User_Detail);
            db.SaveChanges();

            return Ok(p1_User_Detail);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool P1_User_DetailExists(int id)
        {
            return db.P1_User_Detail.Count(e => e.id == id) > 0;
        }
    }
}
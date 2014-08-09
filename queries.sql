/*
##############################################################
## 						Sounds Of Code						##
##				A GitHub Data Challenge III Entry			##
##						  LeonardA-L						##
##############################################################

##	Queries used on the githubarchive BigQuery project

After each query name is given a number indicating roughly how
much Byte this query will take from your daily quota in Google
BigQuery.

If there's a risk you exceed your daily quota by testing on
the (huge) githubarchive dataset, you can test your queries
with the [publicdata:samples.github_timeline] dataset before
actually running them on the real base.
*/

/* ---- Queries actually used in the project */

/* ### X's fork tree (10 GB) */
/*##############################################################*/
/* Put to a GraphViz format*/
SELECT repository_owner+" -> "+actor_attributes_login
FROM (SELECT repository_name, repository_owner, actor_attributes_login, type FROM [githubarchive:github.timeline])
WHERE type = 'ForkEvent'
AND repository_name = 'BrowserQuest'
/* To avoid billions of results with only one branch, only the sub-branches are selected */
AND repository_owner != 'mozilla'
/*##############################################################*/

/* ### TOP 5000 Repo by Push Frequency (12.8 GB) */
/*##############################################################*/
SELECT pushes, LastPushDate, repository_created_at, repository_language FROM
(SELECT pushes, LastPushDate, repository_created_at, repository_language, pushes*1000/(LastPushDate - repository_created_at) as freq FROM(
SELECT count(*) as pushes, max(TIMESTAMP_TO_SEC(TIMESTAMP(created_at))) as LastPushDate, TIMESTAMP_TO_SEC(TIMESTAMP(repository_created_at)) as repository_created_at, repository_language FROM
(SELECT created_at, repository_created_at, repository_name, repository_language, type FROM [githubarchive:github.timeline])
WHERE type = 'PushEvent'
GROUP EACH BY repository_created_at, repository_name, repository_language
HAVING pushes > 10
)
WHERE LastPushDate - repository_created_at > 2*86400	/* Repos with more than 2 days lifespan */
ORDER BY freq DESC
LIMIT 5000)
/*##############################################################*/

/* ### Events per week (6 GB) */
/*##############################################################*/
SELECT UTC_USEC_TO_WEEK(TIMESTAMP_TO_USEC(created_at),1) as monday, COUNT(*) as events
FROM (SELECT created_at FROM [githubarchive:github.timeline] LIMIT 50) a
GROUP BY monday
/*##############################################################*/


/* ### Get the global size of GitHub's repositories on a given day 7 GB */
/*
Note that, having 140+ GB of data in the githubarchive dataset, trying to do a cartesian product
to get the size for each day would cause Google BigQueries' ressources to exceed.
The only solution is to execute the following request multiple times to get a size = f(t) table
(See /data/QueryGenerator_Beat.ods)
*/
/*##############################################################*/
(SELECT TIMESTAMP_TO_SEC(TIMESTAMP("2012-03-11"))  as TimeDate, SUM(repo_size) as gitHubSize FROM( SELECT MAX(repository_size) as repo_size
FROM (SELECT LEFT(created_at, 10) as created_at, repository_size, TIMESTAMP_TO_SEC(TIMESTAMP(repository_created_at)) as repository_created_at FROM [githubarchive:github.timeline])
WHERE created_at < "2012-03-11"
GROUP EACH BY repository_created_at))
/*##############################################################*/


/* ---- Queries that were eventually not used, or were only
useful for testing and Prooves Of Concept */

/* Fork Ranking */
SELECT repository_owner, repository_name, MAX(repository_forks) as forks
FROM [githubarchive:github.timeline]
GROUP BY repository_owner, repository_name
ORDER BY forks DESC
LIMIT 200

/* Get the URL of a repo created at a given time.
Used to find *that* repository with a 30 pushes per second frequency*/
SELECT repository_url FROM
(SELECT repository_created_at, repository_url FROM [publicdata:samples.github_timeline])
WHERE TIMESTAMP_TO_SEC(TIMESTAMP(repository_created_at)) = 1335671558

/* Top 1000 Star Ranking */
SELECT count(type) as stars, repository_url FROM [publicdata:samples.github_timeline]
WHERE type = 'WatchEvent'
GROUP BY repository_url
ORDER BY stars DESC
LIMIT 1000

/* First and last data available */
SELECT MIN(created_at) as min, max(created_at) as max FROM
(SELECT created_at FROM [githubarchive:github.timeline])
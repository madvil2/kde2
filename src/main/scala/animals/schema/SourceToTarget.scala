package animals.schema

import org.squeryl.annotations.Column

case class SourceToTarget(@Column("source_id") sourceId: Long, @Column("target_id") targetId: Long)

-- ----------------------------
--  Procedure structure for `collectSubjectBalance`
-- ----------------------------
use yjz_prod;
DROP PROCEDURE IF EXISTS `collectSubjectBalance`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `collectSubjectBalance`(IN periodId BIGINT)
BEGIN

DECLARE i INT;

SELECT
	max(t2.`level`) INTO i
FROM
	subject_balance t
LEFT JOIN account_subject t2 ON t.book_id = t2.book_id
AND t.subject_code = t2.subject_code;


WHILE i > 1 DO
	INSERT INTO subject_balance (
		subject_code,
		book_id,
		period_id,
		terminal_debit_balance,
		terminal_credit_balance,
		period_debit_occur,
		period_credit_occur,
		year_debit_occur,
		year_credit_occur,
		initial_debit_balance,
		initial_credit_balance,
		profit_loss_occur_amount,
		profit_loss_total_occur_amount
	) SELECT
		sb.parent_subject_code,
		sb.book_id,
		sb.period_id,
		sb.terminal_debit_balance,
		sb.terminal_credit_balance,
		sb.period_debit_occur,
		sb.period_credit_occur,
		sb.year_debit_occur,
		sb.year_credit_occur,
		sb.initial_debit_balance,
		sb.initial_credit_balance,
		sb.profit_loss_occur_amount,
		sb.profit_loss_total_occur_amount
	FROM
		(
			SELECT
				t2.parent_subject_code,
				t.book_id,
				t.period_id,
				NULLIF(IF(t3.direction=1,IFNULL(sum(t.terminal_debit_balance),0)-IFNULL(sum(t.terminal_credit_balance),0),null),0) AS terminal_debit_balance,
				NULLIF(IF(t3.direction=2,IFNULL(sum(t.terminal_credit_balance),0)-IFNULL(sum(t.terminal_debit_balance),0),null),0) AS terminal_credit_balance,
				sum(t.period_debit_occur) AS period_debit_occur,
				sum(t.period_credit_occur) AS period_credit_occur,
				sum(t.year_debit_occur) AS year_debit_occur,
				sum(t.year_credit_occur) AS year_credit_occur,
				NULLIF(IF(t3.direction=1,IFNULL(sum(t.initial_debit_balance),0)-IFNULL(sum(t.initial_credit_balance),0),null),0) AS initial_debit_balance,
				NULLIF(IF(t3.direction=2,IFNULL(sum(t.initial_credit_balance),0)-IFNULL(sum(t.initial_debit_balance),0),null),0) AS initial_credit_balance,
				sum(t.profit_loss_occur_amount) AS profit_loss_occur_amount,
				sum(t.profit_loss_total_occur_amount) AS profit_loss_total_occur_amount
			FROM
				subject_balance t
			LEFT JOIN account_subject t2 ON t.book_id = t2.book_id
			AND t.subject_code = t2.subject_code
			left join account_subject t3 ON t3.book_id = t2.book_id
			AND t3.subject_code = t2.parent_subject_code
			WHERE
				t2.`level` = i
			AND t.period_id = periodId
			GROUP BY
				t2.`level`,
				t2.parent_subject_code,
				t.period_id
		) sb ON DUPLICATE KEY UPDATE period_debit_occur = sb.period_debit_occur,
		period_credit_occur = sb.period_credit_occur,
		year_debit_occur = sb.year_debit_occur,
		year_credit_occur = sb.year_credit_occur,
		terminal_debit_balance = sb.terminal_debit_balance,
		terminal_credit_balance = sb.terminal_credit_balance,
		initial_debit_balance = sb.initial_debit_balance,
		initial_credit_balance = sb.initial_credit_balance,
		profit_loss_occur_amount = sb.profit_loss_occur_amount,
		profit_loss_total_occur_amount = sb.profit_loss_total_occur_amount;


SET i = i - 1;


END
WHILE;


END
 ;;
delimiter ;

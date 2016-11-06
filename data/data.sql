CREATE TABLE `user_info` (
  `id` int(11) NOT NULL,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(20) NOT NULL COMMENT '密码',
  `logtimeTimestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `logging` (
  `log_date` varchar(50) DEFAULT NULL,
  `log_level` varchar(5) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateString` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26020 DEFAULT CHARSET=utf8;

CREATE TABLE `loginip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `ip` varchar(50) NOT NULL,
  `logTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_dygv133t75ldkgsbri57qw81c` (`userId`),
  CONSTRAINT `FK_dygv133t75ldkgsbri57qw81c` FOREIGN KEY (`userId`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=385 DEFAULT CHARSET=utf8;


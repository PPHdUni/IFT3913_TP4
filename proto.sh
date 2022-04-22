#!/bin/sh
# https://github.com/Arkapin/IFT-3913_TP1
# https://github.com/jfree/jfreechart

# verifierClass() {
	# local nc
	# local ncr
	# declare -i nc=0
	# declare -i ncr

	# echo 
	# pwd
	# for FILE in $(ls); 
		# do
			# if [[ $FILE == *"."* ]]
			# then
				# if [[ $FILE == *".java" ]]
				# then
					# echo "Fichier Java: $FILE"
				# else
					# echo "Fichier non-Java: $FILE"
				# fi
			# else
				# echo "Dossier: $FILE"
			# fi
	# done
	
	# for FILE in $(ls); 
		# do	
			# if [[ $FILE == *".java" ]]
			# then
				# nc=$((nc+1))
			# fi
			
			# if [ -d $FILE ];
			# then
				# cd $FILE
				# verifierClass
				# ncr=$?
				# nc=$((ncr+nc))
				# cd ..
			# fi
	# done
	
	# return $nc
# }

DIR="protodepot"
# declare -i ncv=0
rm -rf $DIR

if [ $# -eq 0 ]
    then
        echo "Pas d'argument"
else
	git clone $1 $DIR
fi

javac Proto.java

echo "id_version;NC;mWMC;mcBC" > proto.csv

cd $DIR

#for VERSION in $(git rev-list --max-count=200 --skip=0 master);
for VERSION in $(git rev-list --skip=0 master);
    do
		echo
		git reset --hard $VERSION
		# echo "Version: $VERSION"
		# nc=0
		# verifierClass
		# ncv=$?
		cd ..
		
		java Proto $VERSION
		
		# echo "$VERSION;$nc;" >> proto.csv
		cd $DIR
done

exit

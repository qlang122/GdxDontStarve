#!/usr/bin/python
# -*- coding: UTF-8 -*-

import subprocess
import sys
import os
import re
import argparse

exeFile = 'krane.exe'


def makedir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc:
        if exc.errno != errno.EEXIST:
            raise


def do_exec(in_path, out_path, folder):
    in_dir = '%s' % (in_path)
    out_dir = '%s' % (out_path)
    p = subprocess.Popen(args=[exeFile, "--rename-build", folder, in_dir, out_dir],
                         stdin=subprocess.PIPE, stdout=subprocess.PIPE)
    stdout, stderr = p.communicate()
    if stderr:
        print(stderr)
    print(str(stdout).encode('utf-8'))


def main(in_path, out_path):
    if not os.path.exists(in_path):
        print('[ERROR] \'input path\': %s unexist!' % (in_path))
        return

    print('[INFO] Start convert: %s => %s' % (in_path, out_path))

    if not os.path.isdir(in_path):
        print('[ERROR] \'input path\' must be a directory!')
        return

    for path in os.listdir(in_path):
        folder = path.replace(in_path, '')
        in_folder = os.path.join(in_path, re.sub(r'^[\\/]', '', folder))
        out_folder = os.path.join(out_path, re.sub(r'^[\\/]', '', folder))
        if os.path.isdir(in_folder):
            print('[INFO] %s => %s' % (in_folder, out_folder))
            if not os.path.exists(out_folder):
                makedir_p(out_folder)

            do_exec(in_folder, out_folder, folder)

    print('[INFO] End convert!')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        description='A simple converter that use for spriter file convert to spine file.')
    parser.add_argument('-i', type=str, dest='in_path',
                        help='Directory or File to be converted. By default, point to the current directory.')
    parser.add_argument('-o', type=str, dest='out_path',
                        help='Directory or File convert to. By default, point to the current directory.')
    args = parser.parse_args()

    main(args.in_path or './', args.out_path or './')
